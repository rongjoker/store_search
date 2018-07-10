package com.cyk.es.vo;/**
 * Created by zhangshipeng on 2017/9/7.
 */

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Administrator
 * @ClassName: HttpEntityVo
 * @date 2017-09-07 12:05
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpEntityVo {

    private Map<String,Object> data = new LinkedHashMap<>();  //数据

    //INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
//    USE_PROXY(305, "Use Proxy"),
//    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
//    PERMANENT_REDIRECT(308, "Permanent Redirect"),
//    BAD_REQUEST(400, "Bad Request"),
//    UNAUTHORIZED(401, "Unauthorized"),
//    PAYMENT_REQUIRED(402, "Payment Required"),
//    FORBIDDEN(403, "Forbidden"),
//    NOT_FOUND(404, "Not Found"),
//    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
//    NOT_ACCEPTABLE(406, "Not Acceptable"),
//    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
//    REQUEST_TIMEOUT(408, "Request Timeout"),
//    CONFLICT(409, "Conflict"),
//    GONE(410, "Gone"),
//    LENGTH_REQUIRED(411, "Length Required"),
//    PRECONDITION_FAILED(412, "Precondition Failed"),
//    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    private Integer status=200;  //200成功，500失败，403权限不足，404找不到

    private String message;//消息

    private String errorCode;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void clearData(){
        this.data.clear();;
    }

    public void putData(String key,Object val){
         this.data.put(key,val);
    }


    
}
