package com.guoyuhang.controller;

import com.guoyuhang.pojo.Users;
import com.guoyuhang.pojo.bo.UserBO;
import com.guoyuhang.pojo.vo.UserVO;
import com.guoyuhang.service.UserService;
import com.guoyuhang.goods.utils.CookieUtils;
import com.guoyuhang.goods.utils.GUOGEJSONResult;
import com.guoyuhang.goods.utils.JsonUtils;
import com.guoyuhang.goods.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-03-13 20:32
 */
@RestController
@RequestMapping("passport")
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})

public class PassportController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("usernameIsExist")
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    public GUOGEJSONResult usernameIsExist(@RequestParam String username){
             if(StringUtils.isBlank(username)){
                 return GUOGEJSONResult.errorMsg("用户名不可以空哦");
             }
             boolean b = userService.queryUsernameIsExist(username);
             if(b){
                 return GUOGEJSONResult.errorMsg("用户名已经存在啦");
             }
             return GUOGEJSONResult.ok("此用户名不存在，可以用来注册");
    }
    @PostMapping("/regist")
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    public GUOGEJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
     String username = userBO.getUsername();
     String password = userBO.getPassword();
     String confirmPSW = userBO.getConfirmPassword();
        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPSW)) {
            return GUOGEJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return GUOGEJSONResult.errorMsg("用户名已经存在");
        }

        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return GUOGEJSONResult.errorMsg("密码长度不能少于6");
        }

        // 3. 判断两次密码是否一致
        if (!password.equals(confirmPSW)) {
            return GUOGEJSONResult.errorMsg("两次密码输入不一致");
        }

        // 4. 实现注册
        Users userResult = userService.createUser(userBO);


//用userVO 了 不用这个了
//        userResult = setNullProperty(userResult);


        //实现用户的redis会话
        UserVO userVO = this.convertUsersVO(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userVO), true);


        // TODO 同步购物车数据

        return GUOGEJSONResult.ok("用户注册成功");
    }

    private UserVO convertUsersVO(Users users){
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisTemplate.opsForValue().set(REDIS_USER_TOKEN+":"+users.getId(),uniqueToken);

        //1.可以放到cookie
        //2.令牌封装进 user 选择这种
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(users,userVO);
        userVO.setUserUniqueToken(uniqueToken);
        return userVO;
    }


    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public GUOGEJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return GUOGEJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 实现登录 查询的数据库中的密码  数据库中的密码是加密过的
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));

        if (userResult == null) {
            return GUOGEJSONResult.errorMsg("用户名或密码不正确");
        }

        userResult = setNullProperty(userResult);
        //实现redis会话
        UserVO userVO = this.convertUsersVO(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userVO), true);



        // TODO 同步购物车数据

        return GUOGEJSONResult.ok("登录成功",userResult);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public GUOGEJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);

        return GUOGEJSONResult.ok();
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

}
