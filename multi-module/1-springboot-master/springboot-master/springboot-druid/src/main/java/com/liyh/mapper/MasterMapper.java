package com.liyh.mapper;

import com.liyh.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * mapper接口
 *
 * @Author: liyh
 */
@Mapper
public interface MasterMapper {

    SysUser getMasterInfo(@Param("userId") String userId);

}
