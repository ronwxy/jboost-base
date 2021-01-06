package cn.jboost.base.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 客户端问题导致的异常，如参数校验失败，返回400
 *
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
public class ClientSideException extends BizException {

    public ClientSideException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }
}
