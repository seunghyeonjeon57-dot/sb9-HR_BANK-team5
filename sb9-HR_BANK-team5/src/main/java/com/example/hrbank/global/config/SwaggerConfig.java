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
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwaggerConfig implements CommandLineRunner {

  private final DepartmentRepository departmentRepository;

  @Override
  public void run(String... args) {
    if (departmentRepository.count() == 0) {
      log.info(">>> 초기 데이터 생성을 시작합니다. (빌더 미사용)");

      // 팩트: 빌더 대신 직접 생성자를 호출하여 객체를 만듭니다.
      Department devDept = new Department("개발팀", "sss", LocalDate.of(2024, 1, 1), 1
      );
      Department hrDept = new Department("인사팀", "ddd", LocalDate.of(2024, 2, 2), 1);

      departmentRepository.saveAll(List.of(devDept, hrDept));

      log.info(">>> 초기 부서 데이터 삽입 완료! (ID 확인 필요)");
    }
  }
}