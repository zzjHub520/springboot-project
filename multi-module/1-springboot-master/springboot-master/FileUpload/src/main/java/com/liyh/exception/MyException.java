package com.liyh.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常抛出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyException extends RuntimeException {

    private Integer code;

    private String msg;

    public MyException(String msg) {
        this.msg = msg;
    }

    public MyException(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

}
