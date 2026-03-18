package com.example.hrbank.global.config;

import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.example.hrbank.domain.employee.repository.EmployeeRepository;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.repository.ChangeLogRepository;
import com.example.hrbank.domain.employee.entity.enums.ChangeLogType;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final ChangeLogRepository changeLogRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {

    if (departmentRepository.count() == 0) {
      // 1. 부서 생성
      Department devDept = Department.builder()
          .name("개발팀").description("소프트웨어 개발 및 유지보수").employeeCount(5)
          .establishedDate(LocalDate.of(2024, 1, 1)).build();

      Department hrDept = Department.builder()
          .name("인사팀").description("인재 채용 및 노무 관리").employeeCount(5)
          .establishedDate(LocalDate.of(2024, 1, 1)).build();

      departmentRepository.saveAll(List.of(devDept, hrDept));

      // 2. 랜덤 데이터용 소스
      List<String> lastNames = List.of("김", "이", "박", "최", "정");
      List<String> firstNames = List.of("민준", "서연", "도윤", "서윤", "지호");
      List<String> positions = List.of("백엔드 개발자", "인사담당자", "팀장", "매니저");
      Random random = new Random();

      // 3. 사원 10명 삽입 (Faker 없이 자바 기본 기능 사용)
      IntStream.rangeClosed(1, 50).forEach(i -> {
        String name = lastNames.get(random.nextInt(lastNames.size())) +
            firstNames.get(random.nextInt(firstNames.size()));

        String email = "user" + i + "@hrbank.com";
        String empNo = "EMP-" + (2026000 + i);

        Employee emp = Employee.builder()
            .name(name)
            .email(email)
            .employeeNumber(empNo)
            .department(i <= 5 ? devDept : hrDept)
            .position(positions.get(random.nextInt(positions.size())))
            .hireDate(LocalDate.now().minusDays(random.nextInt(365)))
            .status(EmployeeStatus.ACTIVE)
            .build();
        employeeRepository.save(emp);
      });

      // 4. 로그 기록
      ChangeLog initLog = ChangeLog.builder()
          .employeeNumber("SYSTEM")
          .type(ChangeLogType.CREATED)
          .memo("초기 데이터 10건 생성 완료 (라이브러리 미사용)")
          .build();
      changeLogRepository.save(initLog);

      System.out.println(">>> 초기 데이터 10건이 성공적으로 생성되었습니다!");
    }
  }
}