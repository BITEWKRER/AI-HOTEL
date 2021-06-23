package AI.hotel.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration

public class Swagger2Config {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("AI.hotel.controller"))  //需要生成api的包
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("酒店管理系统 APIS")   // api文档名称
                .description("无更多信息")       // 描述
                .termsOfServiceUrl("https://www.baidu.com/") //服务条款网址
                .version("3.0.1")
                .build();
    }
}
