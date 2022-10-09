package com.liyh.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 异常信息实体类
 */
@Data
@ApiModel(value = "ExceptionLog对象", description = "异常信息表")
public class ExceptionLog implements Serializable {

	private static final long serialVersionUID = 1L;

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
	 * 异常信息
	 */
	@ApiModelProperty(value = "异常信息")
	private String message;
	/**
	 * 操作IP地址
	 */
	@ApiModelProperty(value = "操作IP地址")
	private String remoteIp;
}
