package com.liyh.service;

import com.liyh.entity.ExceptionLog;
import com.liyh.entity.SqlLog;
import com.liyh.entity.User;
import com.liyh.entity.UserLog;

/**
 * @Author: liyh
 * @Date: 2020/9/17 17:17
 */
public interface LogService {
    User query(int id);

    void saveUserLog(UserLog userLog);

    void saveSqlLog(SqlLog sqlLog);

    void saveExceptionLog(ExceptionLog exceptionLog);
}
