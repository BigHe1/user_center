package com.lin.user_center.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author lin
 */
@Data
public class UserRegisterRequest implements Serializable {

    //防止序列化过程中出现冲突
    private static final long serialVersionUID = 9035680834751732032L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
