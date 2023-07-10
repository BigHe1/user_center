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
import java.util.stream.Collectors;

import static com.lin.user_center.constant.UserConstant.ADMIN_ROLE;
import static com.lin.user_center.constant.UserConstant.USER_LOGIN_INFO;

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

    /**
     * 查询用户
     * @param username
     * @return
     */
    @GetMapping("/findAll")
    public List<User> findAll(String username, HttpServletRequest request){

        if(!isAdmin(request)){
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }

        List<User> userList = userService.list(queryWrapper);

        //遍历userList每个元素，脱敏所有信息，再拼成一个完整的userList
        return userList.stream().map(user -> userService.getSafytyUser(user)).collect(Collectors.toList());
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody Long id, HttpServletRequest request){

        if(!isAdmin(request)){
            return false;
        }

         if(id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    private boolean isAdmin(HttpServletRequest request){

        User userInfo = (User) request.getSession().getAttribute(USER_LOGIN_INFO);
        //登录信息为空或者不是管理员就返回空数组
        return userInfo != null && userInfo.getUserRole() == ADMIN_ROLE;
    }

}
