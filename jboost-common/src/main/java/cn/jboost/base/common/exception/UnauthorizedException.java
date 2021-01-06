package cn.jboost.base.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 访问未授权异常，返回401
 *
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
public class UnauthorizedException extends BizException {

    public UnauthorizedException(String message, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, message, cause);
    }
}
