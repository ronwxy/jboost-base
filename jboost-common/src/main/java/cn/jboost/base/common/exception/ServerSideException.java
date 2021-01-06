package cn.jboost.base.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 服务端内部异常，返回500
 *
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
public class ServerSideException extends BizException {

    public ServerSideException(String message, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }
}
