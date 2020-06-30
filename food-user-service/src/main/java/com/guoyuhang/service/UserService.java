package com.guoyuhang.service;


import com.guoyuhang.pojo.Users;
import com.guoyuhang.pojo.bo.UserBO;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-03-13 20:23
 */
public interface UserService {
    public boolean queryUsernameIsExist(String username);
    public Users createUser(UserBO userBO);
    Users queryUserForLogin(String username, String password);


}
