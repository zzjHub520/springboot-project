package com.liyh.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyh.entity.User;
import com.liyh.mapper.UserMapper;
import com.liyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务实现类
 * @Author: liyh
 * @Date: 2020/8/28 11:53
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
