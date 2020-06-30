package com.guoyuhang.controller;


import com.guoyuhang.pojo.UserAddress;
import com.guoyuhang.pojo.bo.AddressBO;
import com.guoyuhang.service.AddressService;
import com.guoyuhang.goods.utils.GUOGEJSONResult;
import com.guoyuhang.goods.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-04-24 0:13
 */
@Api(value = "地址相关",tags = {"地址相关API接口"})
@RestController
@RequestMapping("address")
public class AddressController {
    /**
     * 1.查询用户所有地址列表
     * 2.增加 删除 修改  设置默认地址
     */
    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public GUOGEJSONResult list(
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return GUOGEJSONResult.errorMsg("");
        }

        List<UserAddress> list = addressService.queryAll(userId);
        return GUOGEJSONResult.ok(list);
    }

    @ApiOperation(value = "根据用户id,地址id查询收货地址列表", notes = "根据用户id,地址id查询收货地址列表", httpMethod = "POST")
    @GetMapping("/listOne")
    public UserAddress queryUserAddress(
            @RequestParam String userId,@RequestParam String addressId) {

        if (StringUtils.isBlank(userId)) {
            return null;
        }
        if (StringUtils.isBlank(addressId)) {
            return null;
        }

        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        return userAddress;
    }
    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public GUOGEJSONResult add(@RequestBody AddressBO addressBO) {

        GUOGEJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.addNewUserAddress(addressBO);

        return GUOGEJSONResult.ok();
    }
    private GUOGEJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return GUOGEJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return GUOGEJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return GUOGEJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return GUOGEJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return GUOGEJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return GUOGEJSONResult.errorMsg("收货地址信息不能为空");
        }

        return GUOGEJSONResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public GUOGEJSONResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return GUOGEJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }

        GUOGEJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.updateUserAddress(addressBO);

        return GUOGEJSONResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public GUOGEJSONResult delete(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return GUOGEJSONResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId, addressId);
        return GUOGEJSONResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public GUOGEJSONResult setDefalut(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return GUOGEJSONResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId, addressId);
        return GUOGEJSONResult.ok();
    }
}
