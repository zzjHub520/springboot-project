package com.liyh.service.impl;

import com.liyh.entity.SysUser;
import com.liyh.mapper.MasterMapper;
import com.liyh.service.UserService;
import com.liyh.test.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户业务实现类
 *
 * @Author: liyh
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private MasterMapper masterMapper;

    @Resource
    private TestMapper testMapper;

    @Override
    public SysUser getMasterInfo(String userId) {
        return masterMapper.getMasterInfo(userId);
    }

    @Override
    public SysUser getTestInfo(String userId) {
        return testMapper.getTestInfo(userId);
    }

}
