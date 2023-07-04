package com.lin.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.user_center.service.UserService;
import com.lin.user_center.model.domain.User;
import com.lin.user_center.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author lin
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-07-03 13:04:24
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




