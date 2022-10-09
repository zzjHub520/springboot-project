package com.liyh.agreement;

import lombok.*;

@Data
public class MessageInfo {

    /**
     * 用户数据区长度
     **/
    private int dataLength;

    /**
     * 报文类型
     **/
    private String dirName;

    /**
     * 报文发送者
     **/
    private String prmName;

    /**
     * 功能码名称
     **/
    private String functionName;

    /**
     * 行政区划码
     **/
    private String regionCode;

    /**
     * 终端地址
     **/
    private String terminalAddress;

    /**
     * 主站地址和组地址
     **/
    private String stationAddress;

    /**
     * 应用层功能码名称
     **/
    private String applicationName;

    /**
     * 帧时间标签有效位
     **/
    private String tpvName;

    /**
     * 帧类型
     **/
    private String fiName;

    /**
     * 请求确认标志
     **/
    private String conName;

    /**
     * pn fn
     **/
    private String command;

}
