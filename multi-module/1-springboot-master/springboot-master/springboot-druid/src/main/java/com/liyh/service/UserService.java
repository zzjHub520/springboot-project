package com.liyh.service;

import com.liyh.entity.SysUser;

/**
 * 用户业务接口
 *
 * @Author: liyh
 */
public interface UserService {

    SysUser getMasterInfo(String userId);

    SysUser getTestInfo(String userId);

}
