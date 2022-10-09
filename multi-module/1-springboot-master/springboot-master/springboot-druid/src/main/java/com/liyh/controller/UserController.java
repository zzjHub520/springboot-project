package com.liyh.controller;

import com.liyh.entity.SysUser;
import com.liyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liyh
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getMasterInfo")
    public SysUser getMasterInfo(String userId) {
        SysUser user = userService.getMasterInfo(userId);
        return user;
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getTestInfo")
    public SysUser getTestInfo(String userId) {
        SysUser user = userService.getTestInfo(userId);
        return user;
    }

}
