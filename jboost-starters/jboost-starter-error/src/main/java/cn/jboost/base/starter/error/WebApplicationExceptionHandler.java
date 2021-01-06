package cn.jboost.base.starter.error;

import cn.jboost.base.common.constant.ErrorCodeEnum;
import cn.jboost.base.common.exception.BizException;
import cn.jboost.base.common.util.ExceptionUtil;
import cn.jboost.base.common.util.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.List;

/**
 * 统一异常处理类
 * @Author ronwxy
 * @Date 2020/5/26 9:09
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class WebApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private boolean includeStackTrace;

    public WebApplicationExceptionHandler(boolean includeStackTrace) {
        super();
        this.includeStackTrace = includeStackTrace;
    }

    /**
     * 处理参数校验异常
     *
     * @param ex
     * @return
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        String message = "";
        BindingResult result = ex.getBindingResult();
        //组装校验错误信息
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            StringBuilder errorMsgBuffer = new StringBuilder();
            for (ObjectError error : list) {
                if (error instanceof FieldError) {
                    FieldError errorMessage = (FieldError) error;
                    errorMsgBuffer.append(errorMessage.getDefaultMessage()).append(",");
                }
            }
            //返回信息格式处理
            message = errorMsgBuffer.toString().substring(0, errorMsgBuffer.length() - 1);
        }
        return this.asResponseEntity(HttpStatus.BAD_REQUEST, message, ex);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<Object> handleBizException(BizException ex) {
        logger.warn("catch biz exception: " + ex.toString(), ex.getCause());
        return this.asResponseEntity(HttpStatus.valueOf(ex.getStatus()), ex.getMessage(), ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        logger.warn("catch access denied exception.", ex);
        return this.asResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), ex);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
        logger.warn("catch illegal exception.", ex);
        return this.asResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        logger.error("catch exception.", ex);
        return this.asResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeEnum.INNER_ERROR.getMessage(), ex);
    }

    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        logger.warn("catch unchecked exception.", ex);
        return this.asResponseEntity(status, ex.getMessage(), ex);
    }

    protected ResponseEntity<Object> asResponseEntity(HttpStatus status, String message, Exception ex) {
        ResponseWrapper errorResponse = new ResponseWrapper(status.value(), message);
        //是否包含异常的stack trace
        if (includeStackTrace) {
            errorResponse.setTrace(ExceptionUtil.extractStackTrace(ex.getCause()));
        }
        return new ResponseEntity<>(errorResponse, status);
    }

}
