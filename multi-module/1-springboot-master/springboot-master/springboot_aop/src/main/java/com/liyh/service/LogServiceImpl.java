package com.liyh.service;

import com.liyh.entity.ExceptionLog;
import com.liyh.entity.SqlLog;
import com.liyh.entity.User;
import com.liyh.entity.UserLog;
import com.liyh.mapper.LogMapper;
import com.liyh.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: liyh
 * @Date: 2020/9/17 17:18
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public User query(int id) {
        return logMapper.query(id);
    }

    @Override
    public void saveUserLog(UserLog userLog) {
        logMapper.saveUserLog(userLog);
    }

    @Override
    public void saveSqlLog(SqlLog sqlLog) {
        logMapper.saveSqlLog(sqlLog);
    }

    @Override
    public void saveExceptionLog(ExceptionLog exceptionLog) {
        logMapper.saveExceptionLog(exceptionLog);
    }
}
