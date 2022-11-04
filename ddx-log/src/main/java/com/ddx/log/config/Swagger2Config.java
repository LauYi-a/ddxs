package com.ddx.log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Swagger2Config
 * @Description: Swagger2API
 * 访问地址：http://localhost:9999/ddx/xxx/doc.html
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {

    	ParameterBuilder tokenPara = new ParameterBuilder();
    	List<Parameter> params = new ArrayList<Parameter>();
    	tokenPara.name("Authorization")
		    	.description("认证token")
				.defaultValue("bearer ")
		    	.modelRef(new ModelRef("string"))
		    	.parameterType("header")
		    	.required(false)
		    	.build();
    	params.add(tokenPara.build());

    	return new Docket(DocumentationType.SWAGGER_2)
    			.globalOperationParameters(params)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("DDX-LOG")
                //创建人
                .contact(new Contact("ddx", "ddx@163.com", "ddx@163.com"))
                //版本号
                .version("1.0")
                //描述
                .description("DDX 后端系统管理")
                .build();
    }
}
