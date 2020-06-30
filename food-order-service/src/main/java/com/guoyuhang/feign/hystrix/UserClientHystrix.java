package com.guoyuhang.feign.hystrix;

import com.guoyuhang.feign.UserClient;
import com.guoyuhang.pojo.UserAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-06-20 20:13
 */
@Component
@Slf4j
public class UserClientHystrix implements UserClient {
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        log.info("queryUserAddress error,go hystrix");
        return null;
    }
}
