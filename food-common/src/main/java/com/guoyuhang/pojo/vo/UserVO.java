package com.guoyuhang.pojo.vo;

import lombok.Data;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-03-25 18:19
 */
@Data
public class UserVO {
    private String id;

    /**
     * 用户名 用户名
     */
    private String username;
    private String nickname;
    private String face;
    private Integer sex;
    private String userUniqueToken;

}
