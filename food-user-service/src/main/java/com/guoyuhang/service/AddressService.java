package com.guoyuhang.service;

import com.guoyuhang.pojo.UserAddress;
import com.guoyuhang.pojo.bo.AddressBO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-04-24 0:22
 */
@Service
public interface AddressService {

    List<UserAddress> queryAll(String userId);
    void addNewUserAddress(AddressBO addressBO);
    public UserAddress queryUserAddress(String userId, String addressId);
    void updateUserAddress(AddressBO addressBO);
    void deleteUserAddress(String userId, String addressId);
    void updateUserAddressToBeDefault(String userId, String addressId);
//    UserAddress queryUserAddres(String userId, String addressId);
}
