package com.example.hrbank.global.config;

import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    // 1. 팩트: 로컬 테스트를 위한 8080 포트 설정
    Server localServer = new Server()
        .url("http://localhost:8080")
        .description("Local Server (Port 8080)");

    // 2. 운영 서버 (AWS ELB)
    Server prodServer = new Server()
        .url("http://sprint-project-1196140422.ap-northeast-2.elb.amazonaws.com/sb/hrbank")
        .description("Production Server (AWS)");

    return new OpenAPI()
        .servers(List.of(localServer, prodServer))
        .info(new Info()
            .title("HR Bank API")
            .description("HR Bank API 문서입니다.")
            .version("v1.0"));
  }
}