package com.dkh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private Object data;
    private String msg;

    public Result(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }


    public static Result fail(Integer code, String msg){
        Result r = new Result();
        r.code = code;
        r.msg = msg;
        return r;
    }
    public static Result success(Object data, String msg){
        return new Result(200, data, msg);
    }
    public static Result success(String msg){
        return new Result(200, null, msg);
    }
    public static Result success(Object data){
        return new Result(200, data, "OK");
    }
    public static Result success(Integer code, Object data){
        return new Result(code, data);
    }
}
