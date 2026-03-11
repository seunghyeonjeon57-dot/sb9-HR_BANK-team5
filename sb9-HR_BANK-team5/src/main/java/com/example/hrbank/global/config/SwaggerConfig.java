package com.example.hrbank.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI hrBankOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("HR Bank API")
            .description("HR Bank API 문서")
            .version("v1.0"))
        .servers(List.of(new Server()
            .url("http://sprint-project-1196140422.ap-northeast-2.elb.amazonaws.com/sb/hrbank")
            .description("운영 서버")));
  }
}