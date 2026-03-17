package com.example.hrbank.domain.employee.controller;


import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.employee.controller.api.EmployeeControllerApi;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDistributionDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeTrendDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeCreateRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import com.example.hrbank.domain.employee.dto.request.StatRequest;
import com.example.hrbank.domain.employee.dto.request.StatsCountRequest;
import com.example.hrbank.domain.employee.dto.request.StatsDepartmentRequest;
import com.example.hrbank.domain.employee.repository.EmployeeRepository;
import com.example.hrbank.domain.employee.service.EmployeeService;
import com.example.hrbank.domain.employee.service.EmployeeStatsService;
import com.example.hrbank.domain.employee.service.Impl.EmployeeServiceImpl;
import com.example.hrbank.global.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeControllerApi {
  private final EmployeeService employeeService;
  private final EmployeeStatsService employeeStatsService;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("employee") EmployeeCreateRequest employeeCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(employeeService.createEmployee(employeeCreateRequest, profile));
  }

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> searchEmployees(@ModelAttribute EmployeeSearchRequest request) {
    return ResponseEntity.ok(employeeService.searchEmployees(request));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> searchEmployeeById(@PathVariable("id") Long id) { // 팩트: @PathVariable 필수
    return ResponseEntity.ok(employeeService.searchEmployeeById(id));
  }

  @Override
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable("id") Long id,
      @RequestPart("employee") EmployeeUpdateRequest employeeUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      HttpServletRequest request
  ) {
    String ipAddress = IpUtil.getUserIp(request);
    return ResponseEntity.ok(employeeService.updateEmployee(id, employeeUpdateRequest,profile,ipAddress));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id,HttpServletRequest request) {

    String ipAddress = IpUtil.getUserIp(request);
    employeeService.deleteEmployee(id,ipAddress);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/stats/trend")
  public ResponseEntity<List<EmployeeTrendDto>> getStatsTrend(StatRequest request) {
    return ResponseEntity.ok(employeeStatsService.getEmployeeTrend(request.from(),request.to(),request.unit()));
  }

  @Override
  @GetMapping("/stats/distribution")
  public ResponseEntity<List<EmployeeDistributionDto>> getStatsDistribution(StatsDepartmentRequest request) {
    return ResponseEntity.ok(employeeStatsService.getEmployeeDistribution(request.groupBy(), request.status()));
  }

  @Override
  @GetMapping("/count")
  public ResponseEntity<Long> countEmployee(@ModelAttribute StatsCountRequest request) {
    return ResponseEntity.ok(employeeStatsService.getEmployeeCount(request.status(),request.fromDate(),request.toDate()));
  }

}