package cn.jboost.base.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 禁止访问异常，返回403
 *
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
public class ForbiddenException extends BizException {

    public ForbiddenException(String message, Throwable cause) {
        super(HttpStatus.FORBIDDEN, message, cause);
    }
}
