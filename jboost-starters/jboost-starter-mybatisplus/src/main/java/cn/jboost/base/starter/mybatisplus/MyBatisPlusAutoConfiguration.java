package cn.jboost.base.starter.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass({PaginationInterceptor.class})
@Import({com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class})
@AutoConfigureAfter(com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class)
public class MyBatisPlusAutoConfiguration {

    /**
     * 注册分页拦截器
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor interceptor = new PaginationInterceptor();
        interceptor.setLimit(1000); //最大单页限制数，默认 500 条，-1 不受限制
        return interceptor;
    }

    /**
     * 注册创建时间/更新时间处理拦截器
     * @return
     */
    @Bean
    public TimeSqlInterceptor sqlInterceptor() {
        return new TimeSqlInterceptor();
    }

}
