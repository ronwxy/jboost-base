package cn.jboost.base.starter.logging.config;

import cn.hutool.core.util.ReflectUtil;
import cn.jboost.base.starter.logging.aspect.LoggerAspect;
import cn.jboost.base.starter.logging.filter.ReqIdFilter;
import cn.jboost.base.starter.logging.provider.ILogProvider;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * @Author ronwxy
 * @Date 2020/5/28 20:19
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(LoggerConfigProperties.class)
@ConditionalOnClass({Aspect.class, MDC.class})
public class LoggerAutoConfiguration {

    @Autowired
    private LoggerConfigProperties configProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public LoggerAspect loggerAspect() {

        ILogProvider logService;
        try {
            logService = (ILogProvider) applicationContext.getBean(configProperties.getServiceImplClass());
            if (logService == null) {
                logService = (ILogProvider) ReflectUtil.newInstance(configProperties.getServiceImplClass());
            }
        } catch (Exception e) {
            logService = (ILogProvider) ReflectUtil.newInstance(configProperties.getServiceImplClass());
        }
        LoggerAspect loggerAspect = new LoggerAspect(logService, configProperties.getCollectionDepthThreshold());
        return loggerAspect;
    }

    /**
     * 注册一个过滤器，用来生成一个reqId，标记一次请求，将本次请求所产生的日志串联起来
     *
     * @param
     * @return
     */
    @Bean
    public FilterRegistrationBean reqIdFilter() {
        ReqIdFilter reqIdFilter = new ReqIdFilter();
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(reqIdFilter);
        List<String> urlPatterns = Collections.singletonList("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(100);
        return registrationBean;
    }
}
