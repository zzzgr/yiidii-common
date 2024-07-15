package cn.yiidii.base.core.domain;

import cn.yiidii.base.constant.Constant;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应信息
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuppressWarnings("unused")
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    private long t;

    public static <T> R<T> ok() {
        return restResult(null, Constant.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, Constant.SUCCESS, Constant.RESP_SUCCESS);
    }

    public static <T> R<T> ok(String msg, T data) {
        return restResult(data, Constant.SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, Constant.FAIL, Constant.RESP_FAILURE);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, Constant.FAIL, msg);
    }


    public static <T> R<T> failed(T data) {
        return restResult(data, Constant.FAIL, Constant.RESP_FAILURE);
    }

    public static <T> R<T> failed(String msg, T data) {
        return restResult(data, Constant.FAIL, msg);
    }

    public static <T> R<T> failed(int code, String msg) {
        return restResult(null, code, msg);
    }

    public static <T> R<T> failed(int code, String msg, T data) {
        return restResult(data, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setT(System.currentTimeMillis());
        return apiResult;
    }

}
