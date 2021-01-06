package cn.jboost.base.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务异常类，对程序中可能出现的异常进行封装统一处理
 *
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
@Getter
public class BizException extends RuntimeException {
    private int status = HttpStatus.BAD_REQUEST.value();

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status.value();
    }
}
