package com.lin.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.user_center.service.UserService;
import com.lin.user_center.model.domain.User;
import com.lin.user_center.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author lin
* @description 用户服务实现类
* @createDate 2023-07-03 13:04:24
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

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
}




