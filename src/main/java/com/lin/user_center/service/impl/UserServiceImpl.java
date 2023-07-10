package com.lin.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.user_center.service.UserService;
import com.lin.user_center.model.domain.User;
import com.lin.user_center.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lin.user_center.constant.UserConstant.USER_LOGIN_INFO;

/**
* @author lin
* @description 用户服务实现类
* @createDate 2023-07-03 13:04:24
*/
@Service
@Slf4j //日志注解
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    // 盐
    private static final  String SALT = "123";



    //public static final  String USER_LOGIN_INFO = "userLoginState";

    @Resource
    UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验
        if(StringUtils.isAllBlank(userAccount, userPassword, checkPassword)){
            return -1;
        }
        if(userAccount.length() < 4){
            return -1;
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }

        //校验特殊字符
        String vailPattern = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}‘；：”“’。， 、？]";//+号表示空格
        Matcher matcher = Pattern.compile(vailPattern).matcher(userAccount);
        if(matcher.find()){
            return -1;
        }

        //密码二次校验
        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        //账号不能重复(最后再校验数据库，防止浪费
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        if(this.count(wrapper) > 0){
            return -1;
        }

        // 2. 加密
        // 盐
        // Calendar calendar = Calendar.getInstance();
        // final String SALT = calendar.getTime().toString();
        final String SALT = "123";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //1. 校验
        if(StringUtils.isAllBlank(userAccount, userPassword)){
            return null;
        }
        if(userAccount.length() < 4){
            return null;
        }
        if(userPassword.length() < 8){
            return null;
        }

        //校验特殊字符
        String vailPattern = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}‘；：”“’。， 、？]";//+号表示空格
        Matcher matcher = Pattern.compile(vailPattern).matcher(userAccount);
        if(matcher.find()){
            return null;
        }

        // 2. 加密 查询是否存在
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if(user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        //3. 用户脱敏
        User safetyUser = getSafytyUser(user);

        //4. 记录用户登录状态
        //session里attribute的是以map形式储存的
        request.getSession().setAttribute(USER_LOGIN_INFO, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
     public User getSafytyUser(User originUser){
         User safetyUser = new User();
         safetyUser.setId(originUser.getId());
         safetyUser.setUsername(originUser.getUsername());
         safetyUser.setUserAccount(originUser.getUserAccount());
         safetyUser.setAvatarUrl(originUser.getAvatarUrl());
         safetyUser.setGender(originUser.getGender());
         safetyUser.setPhone(originUser.getPhone());
         safetyUser.setEmail(originUser.getEmail());
         safetyUser.setUserRole(originUser.getUserRole());
         safetyUser.setUserStatus(originUser.getUserStatus());
         safetyUser.setCreateTime(originUser.getCreateTime());
         return safetyUser;
     }
}




