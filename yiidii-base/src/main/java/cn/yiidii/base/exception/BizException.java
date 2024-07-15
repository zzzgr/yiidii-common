package cn.yiidii.base.exception;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.base.util.MessageUtils;
import cn.yiidii.base.util.Util;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

/**
 * 业务异常
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BizException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    private Object[] args;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(String message, Object... args) {
        this.message = message;
        this.args = args;
    }

    public BizException(Integer code, String message, Object... args) {
        this.code = code;
        this.message = message;
        this.args = args;
    }


    @Override
    public String getMessage() {
        return Util.ifNull(MessageUtils.message(message, args), message);
    }
}
