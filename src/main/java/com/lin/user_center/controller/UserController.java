package com.lin.user_center.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.user_center.model.domain.User;
import com.lin.user_center.model.domain.request.UserLoginRequest;
import com.lin.user_center.model.domain.request.UserRegisterRequest;
import com.lin.user_center.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return 用户id
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){

        if(userRegisterRequest == null)
            return null;

        String userAccount = userRegisterRequest.getUserAccount();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userPassword = userRegisterRequest.getUserPassword();

        if(StringUtils.isAllBlank(userAccount, userPassword, checkPassword))
            return null;

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return 用户信息
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){

        if(userLoginRequest == null)
            return null;

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAllBlank(userAccount, userPassword))
            return null;

        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/findAll")
    public List<User> findAll(@RequestBody String username){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        return userService.list(queryWrapper);
    }

}
