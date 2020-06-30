package com.guoyuhang.feign;

import com.guoyuhang.feign.hystrix.UserClientHystrix;
import com.guoyuhang.goods.utils.GUOGEJSONResult;
import com.guoyuhang.pojo.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-06-20 20:10
 */
@FeignClient(name = "users",
        fallback = UserClientHystrix.class)
@Component
public interface UserClient {
    @RequestMapping(value ="/address/listOne",method = RequestMethod.GET )
    public UserAddress queryUserAddress(
            @RequestParam String userId, @RequestParam String addressId);
}


