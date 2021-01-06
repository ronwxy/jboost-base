package cn.jboost.base.starter.error;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.Servlet;

/**
 * 异常处理配置类
 *
 * @Author ronwxy
 * @Date 2020/5/26 9:09
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@ConditionalOnMissingBean(ResponseEntityExceptionHandler.class)
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class ExceptionHandlerAutoConfiguration {

    @Profile({"test", "formal", "prod"})
    @Bean
    public ResponseEntityExceptionHandler defaultGlobalExceptionHandler() {
        //测试、正式环境，不输出异常的stack trace
        return new WebApplicationExceptionHandler(false);
    }

    @Profile({"default","local","dev"})
    @Bean
    public ResponseEntityExceptionHandler devGlobalExceptionHandler() {
        //本地、开发环境，输出异常的stack trace
        return new WebApplicationExceptionHandler(true);
    }

    /**
     * 未经过Controller抛出的异常处理，如Filter抛出异常，在BasicErrorController的error方法中调用
     * @return
     */
    @Profile({"test", "formal", "prod"})
    @Bean
    public ErrorAttributes basicErrorAttributes() {
        //测试、正式环境，不输出异常的stack trace
        return new BaseErrorAttributes(false);
    }

    @Profile({"default", "local", "dev"})
    @Bean
    public ErrorAttributes devBasicErrorAttributes() {
        //本地、开发环境，输出异常的stack trace
        return new BaseErrorAttributes(true);
    }
}
