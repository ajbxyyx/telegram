package com.ajbxyyx.common.entities;


import java.io.Serializable;

//@ApiModel("基础返回体")
public class ApiResult<T> implements Serializable {

//    @ApiModelProperty("成功标识")
    private Boolean ok;
//    @ApiModelProperty("错误码")
    private String code;
//    @ApiModelProperty("错误消息")
    private String errMsg;
//    @ApiModelProperty("返回数据")
    private T data;


    public static <T> ApiResult<T> success(){
        ApiResult<T> result = new ApiResult<T>();
        result.setOk(Boolean.TRUE);
        return result;
    }

    public static <T> ApiResult<T> success(T data){
        ApiResult<T> result = new ApiResult<T>();
        result.setOk(Boolean.TRUE);
        result.setData(data);
        return result;
    }
    public static <T> ApiResult<T> fail(Integer code, String errMsg){
        ApiResult<T> result = new ApiResult<T>();
        result.setOk(Boolean.FALSE);
        result.setCode(String.valueOf(code));
        result.setErrMsg(errMsg);
        return result;
    }



    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
