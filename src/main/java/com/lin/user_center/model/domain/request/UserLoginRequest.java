package com.lin.user_center.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author lin
 */
@Data
public class UserLoginRequest implements Serializable {

    //防止序列化过程中出现冲突
    private static final long serialVersionUID = 9035680834751732032L;

    private String userAccount;
    private String userPassword;

}
