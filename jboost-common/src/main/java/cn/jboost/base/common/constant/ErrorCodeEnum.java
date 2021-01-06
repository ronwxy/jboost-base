package cn.jboost.base.common.constant;

/**
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
public enum ErrorCodeEnum {
    LOGIN_BAD_CREDENTIALS("用户名或密码错误"),
    LOGIN_INVALID_CODE("验证码错误"),
    LOGIN_MISSING_USER_TYPE("用户类型未提供"),
    LOGIN_UNSUPPORTED_TYPE("不支持的登录类型"),
    LOGIN_UNSUPPORTED_METHOD("请使用 POST/json 登录"),
    LOGIN_PARSE_FAILED("登录参数解析失败"),
    LOGIN_RETRY_LIMITED("密码重试超过最大次数，请十分钟后再试或联系管理员"),

    CODE_SEND_FAIL("发送验证码失败，请稍后重试"),
    CODE_CHECK_FAIL("验证码错误或已过期"),

    BLOCKED("访问被限制，请稍后再试"),
    FORBIDDEN("操作没有权限"),
    NOT_EXIST("请求的资源不存在"),
    VALIDATE_FAIL("参数校验失败"),
    UNAUTHORIZED("登录凭证已失效，请重新登录"),
    TOKEN_EXPIRED("登录状态已过期"),
    INNER_ERROR("抱歉，服务出错啦，请稍后重试"),
    TIMEOUT_ERROR("请求服务超时，请稍后重试"),
    SORT_ERROR("排序格式不符合规范，示例：sort=createTime,asc,age,desc"),
    SORT_LIMIT("升序、降序部分最多均只支持两个字段"),
    COLUMN_ABSENT("不存在对应的列")
    ;

    private String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
