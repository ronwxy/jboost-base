package cn.jboost.base.starter.web;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * api页面 /swagger-ui.html
 *
 * @Author ronwxy
 * @Date 2020/7/2 9:35
 * @Version 1.0
 */

@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration {

    @Value("${swagger.title:服务端接口文档}")
    private String title;

    @Value("${swagger.version:1.0}")
    private String version;

    @Value("${swagger.tokenHeader:Authorization}")
    private String tokenHeader;

    @Value("${swagger.enabled:false}")
    private Boolean enabled;

    @Value("${swagger.basePackages}")
    private String[] basePackages;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .build();
    }

    @Bean
    public Docket docket() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar
                .name(tokenHeader).description("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("Bearer ")
                .required(true)
                .build();
        pars.add(ticketPar.build());

        // 配置扫描
        Predicate<RequestHandler> or = null;
        if (basePackages != null && basePackages.length > 0) {
            Predicate<RequestHandler>[] predicates = new Predicate[basePackages.length];
            for (int i = 0; i < predicates.length; i++) {
                predicates[i] = RequestHandlerSelectors.basePackage(basePackages[i]);
            }
            or = Predicates.or(predicates);
        }

        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .apiInfo(apiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")));
        if (or != null) {
            builder.apis(or);
        }
        return builder.build().globalOperationParameters(pars);
    }


}
