package com.lin.user_center.service;

import com.lin.user_center.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author lin
* @description 用户服务
* @createDate 2023-07-03 13:04:24
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息 user
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafytyUser(User originUser);
}
