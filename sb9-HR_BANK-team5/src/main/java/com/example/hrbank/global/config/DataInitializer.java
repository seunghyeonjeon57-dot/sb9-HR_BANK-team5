package com.example.hrbank.global.config;

import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final DepartmentRepository departmentRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {

    if (departmentRepository.count() == 0) {
      Department dept1 = Department.builder()
          .name("개발팀")
          .description("소프트웨어 개발 및 유지보수")
          .employeeCount(10)
          .establishedDate(LocalDate.of(2024, 1, 1)) // 팩트: 날짜 누락 해결
          .build();
      Department dept2 = Department.builder()
          .name("인사팀")
          .description("인재 채용 및 노무 관리")
          .employeeCount(10)
          .establishedDate(LocalDate.of(2024, 1, 1)) // 팩트: 날짜 누락 해결
          .build();
      departmentRepository.saveAll(List.of(dept1, dept2));
      System.out.println(">>> 초기 부서 데이터 삽입 완료!");
    }
  }
}