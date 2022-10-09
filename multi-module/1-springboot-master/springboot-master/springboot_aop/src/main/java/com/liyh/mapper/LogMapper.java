package com.liyh.mapper;

import com.liyh.entity.ExceptionLog;
import com.liyh.entity.SqlLog;
import com.liyh.entity.User;
import com.liyh.entity.UserLog;

public interface LogMapper {

    User query(int id);

    void saveUserLog(UserLog userLog);

    void saveSqlLog(SqlLog sqlLog);

    void saveExceptionLog(ExceptionLog exceptionLog);
}
