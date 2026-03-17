package com.example.hrbank.global.config;

import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final DepartmentRepository departmentRepository;

  @Override
  public void run(String... args) throws Exception {

    if (departmentRepository.count() == 0) {
      Department dept1 = Department.builder().name("개발팀").build();
      Department dept2 = Department.builder().name("인사팀").build();

      departmentRepository.saveAll(List.of(dept1, dept2));
      System.out.println(">>> 초기 부서 데이터 삽입 완료!");
    }
  }
}