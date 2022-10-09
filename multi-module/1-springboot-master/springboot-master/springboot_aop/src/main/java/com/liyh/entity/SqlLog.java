package com.liyh.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * sql日志实体类
 */
@Data
@ApiModel(value = "SqlLog对象", description = "sql日志实体类")
public class SqlLog implements Serializable {
	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Integer id;
	/**
	 * 请求id
	 */
	@ApiModelProperty(value = "请求id")
	private String requestId;
	/**
	 * sql
	 */
	@ApiModelProperty(value = "sql")
	private String sql;
	/**
	 * 方法名
	 */
	@ApiModelProperty(value = "方法名")
	private String methodName;
	/**
	 * 方法类
	 */
	@ApiModelProperty(value = "方法类")
	private String methodClass;
	/**
	 * 请求URI
	 */
	@ApiModelProperty(value = "请求URI")
	private String requestUrl;
	/**
	 * 操作IP地址
	 */
	@ApiModelProperty(value = "操作IP地址")
	private String remoteIp;
	/**
	 * 执行时间
	 */
	@ApiModelProperty(value = "执行时间")
	private Long loadTime;
}
