package lxdemo.springcloud.systemAdmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.show:true}")
    private boolean swaggerShow;

    /**
     * @return
     */
    @Bean
    public Docket api() {
        if (swaggerShow) {
            ParameterBuilder tokenPar = new ParameterBuilder();  
            List<Parameter> pars = new ArrayList<Parameter>();  
            tokenPar.name("Authorization").description("jwt token").modelRef(new ModelRef("Authorization")).parameterType("header").required(false).defaultValue("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjbWNjIiwiZXhwIjoyMjA0MTc0OTY0LCJpYXQiOjE1NzM0NTQ5NjQsInVzZXJJZCI6IjEifQ.PrPXJz9eKRDz5vcsnNKj6H53nQ1FTHysogt1Gdhto7w").build();  
            pars.add(tokenPar.build());
            return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiOfflineInfo()).select()  
                    .apis(RequestHandlerSelectors.basePackage("com.hy.cmcc.bj.systemAdmin"))
                    .paths(PathSelectors.any()).build().globalOperationParameters(pars);
        } else {
            return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiOnlineInfo()).select()
                    .paths(PathSelectors.none()).build();
        }
    }

    private ApiInfo apiOfflineInfo() {
        return new ApiInfoBuilder().title("系统管理Api").description("系统管理Api文档").version("1.0.0").build();
    }

    private ApiInfo apiOnlineInfo() {
        return new ApiInfoBuilder().title("Api").description("Api").license("CMCC2016").version("1.0.0").build();
    }

}