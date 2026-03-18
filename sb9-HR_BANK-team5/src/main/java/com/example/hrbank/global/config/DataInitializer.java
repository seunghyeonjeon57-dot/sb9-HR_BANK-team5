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
  private final ChangeLogRepository changeLogRepository; // 로그 기록을 위해 다시 추가

  @Override
  @Transactional
  public void run(String... args) throws Exception {

    // 1. 부서가 하나도 없을 때만 초기 데이터를 생성합니다.
    if (departmentRepository.count() == 0) {

      // 2. 부서 생성 (각 팀당 25명씩 설정)
      Department devDept = Department.builder()
          .name("개발팀")
          .description("소프트웨어 개발 및 유지보수")
          .employeeCount(25)
          .establishedDate(LocalDate.of(2024, 1, 1))
          .build();

      Department hrDept = Department.builder()
          .name("인사팀")
          .description("인재 채용 및 노무 관리")
          .employeeCount(25)
          .establishedDate(LocalDate.of(2024, 1, 1))
          .build();

      departmentRepository.saveAll(List.of(devDept, hrDept));

      // 3. 랜덤 데이터용 소스
      List<String> lastNames = List.of("김", "이", "박", "최", "정");
      List<String> firstNames = List.of("민준", "서연", "도윤", "서윤", "지호");
      List<String> positions = List.of("백엔드 개발자", "인사담당자", "팀장", "매니저");
      Random random = new Random();

      // 4. 사원 50명 삽입 (1~25 개발팀, 26~50 인사팀)
      IntStream.rangeClosed(1, 50).forEach(i -> {
        String name = lastNames.get(random.nextInt(lastNames.size())) +
            firstNames.get(random.nextInt(firstNames.size()));

        String email = "user" + i + "@hrbank.com";
        String empNo = "EMP-" + (2026000 + i);

        Department targetDept = (i <= 25) ? devDept : hrDept;

        Employee emp = Employee.builder()
            .name(name)
            .email(email)
            .employeeNumber(empNo)
            .department(targetDept)
            .position(positions.get(random.nextInt(positions.size())))
            .hireDate(LocalDate.now().minusDays(random.nextInt(365)))
            .status(EmployeeStatus.ACTIVE)
            .build();

        employeeRepository.save(emp);
      });

      // 5. 모든 데이터 생성이 끝난 후 마지막에 한 번만 요약 로그 기록
      ChangeLog summaryLog = ChangeLog.builder()
          .employeeNumber("SYSTEM")
          .type(ChangeLogType.CREATED)
          .memo("시스템 초기화: 부서 2건 및 사원 50건(개발 25, 인사 25) 생성 완료")
          .build();
      changeLogRepository.save(summaryLog);

      System.out.println(">>> 초기 데이터 50건 및 통합 로그 기록 완료!");
    }
  }
}