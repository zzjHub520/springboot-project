package com.liyh.controller;

import com.liyh.entity.User;
import com.liyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;

/**
 * @Author: liyh
 * @Date: 2020/8/28 11:52
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    //http://localhost:8081/user/getInfo?userId=1
    @RequestMapping("/getInfo")
    public User getInfo(String userId){
        User user = userService.getById(userId);
        System.out.println(user.getName());
        return user;
    }

    /**
     * 查询所有信息
     * @return
     */
    //http://localhost:8081/user/getUserList
    @RequestMapping("/getUserList")
    public List<User> getUserList(){
        return userService.list();
    }

    /**
     * 新增用户信息
     */
    //http://localhost:8081/user/saveInfo
    @RequestMapping("/saveInfo")
    public void saveInfo(){
        User user = new User();
        user.setBirthday(new Date());
        user.setGender("1");
        user.setUsername("apple");
        user.setPassword("apple");
        user.setRemark("消费大师");
        user.setStation("冻结");
        user.setTelephone("111111");
        user.setName("平锅");
        userService.save(user);
    }
}
