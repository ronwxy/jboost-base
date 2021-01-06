package cn.jboost.base.common.constant;

/**
 * @Author ronwxy
 * @Date 2020/5/22 17:28
 * @Version 1.0
 */
public final class Constants {

    public static final String USERNAME_TYPE_DELIMITER = "#";

    public static final String USER_TYPE = "userType";
    public static final String CAPTCHA_REDIS_KEY_PREFIX = "captcha:";
    public static final String PHONE_CODE_REDIS_KEY_PREFIX = "phone_code:";
    public static final String PASSWORD_RETRY_REDIS_KEY_PREFIX = "password_retry:";
    //浏览器默认支持UTF-8,避免某些http client调用乱码
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

}
