package com.example.hrbank.domain.department.service.basic;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentUpdateRequest;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.mapper.DepartmentMapper;
import com.example.hrbank.domain.department.repository.DepartmentRepository;
import com.example.hrbank.domain.department.service.DepartmentService;
import com.example.hrbank.domain.employee.repository.EmployeeRepository;
import com.example.hrbank.global.error.BusinessException;
import com.example.hrbank.global.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicDepartmentService implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;
  private final EmployeeRepository employeeRepository;

  // 1. 부서 등록
  @Override
  @Transactional
  public DepartmentDto create(DepartmentCreateRequest request, Integer employeeCount) {

    if (departmentRepository.existsByName(request.name())) {
      throw new BusinessException(ErrorCode.DEPT_NAME_DUPLICATION);
    }

    Department department = new Department(
        request.name(),
        request.description(),
        request.establishedDate(),
        employeeCount
    );

    departmentRepository.save(department);

    return departmentMapper.toDto(department);
  }

  // 공통 조회
  @Override
  @Transactional(readOnly = true)
  public Department findEntityById(Long id) {
    return departmentRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_NOT_FOUND));
  }

  //여기서 오류 처리 없는 이유는 findEntityById에서 오류가 터지기 때문.
  // 2. 부서 상세 조회
  @Override
  @Transactional(readOnly = true)
  public DepartmentDto find(Long id) {
    Department department = findEntityById(id);
    return departmentMapper.toDto(department);
  }

  // 3. 부서 목록 조회
  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseDepartmentDto findAll(DepartmentSearchRequest request) {

    int size = request.size() != null ? request.size() : 10;

    Long idAfter = 0L;

    if (request.cursor() != null && !request.cursor().isBlank()) {
      try {
        byte[] decodedBytes = Base64.getDecoder().decode(request.cursor());
        String json = new String(decodedBytes);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(json, Map.class);

        idAfter = ((Number) map.get("id")).longValue();

      } catch (Exception e) {
        // ✅ 목록 조회 400 → 공통 코드 사용
        throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
      }
    } else if (request.idAfter() != null) {
      idAfter = request.idAfter();
    }

    String sortField = request.sortField() != null ? request.sortField() : "establishedDate";

    if (!List.of("name", "establishedDate").contains(sortField)) {
      // ✅ 목록 조회 400 → 공통 코드
      throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
    }

    String sortDirection = request.sortDirection() != null ? request.sortDirection() : "asc";

    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);

    Pageable pageable = PageRequest.of(0, size + 1, sort);

    List<Department> departments =
        departmentRepository.searchDepartments(
            request.nameOrDescription(),
            idAfter,
            pageable
        );

    boolean hasNext = departments.size() > size;

    if (hasNext) {
      departments = departments.subList(0, size);
    }

    long totalElements = departmentRepository.count();

    return departmentMapper.toCursorPageDto(
        departments,
        size,
        totalElements,
        hasNext
    );
  }

  // 4. 부서 수정
  @Override
  @Transactional
  public DepartmentDto update(Long id, DepartmentUpdateRequest request) {

    Department department = findEntityById(id);

    if (!department.getName().equals(request.name())
        && departmentRepository.existsByName(request.name())) {

      // ✅ 수정 400 → 중복 이름
      throw new BusinessException(ErrorCode.DEPT_NAME_DUPLICATION);
    }

    department.update(
        request.name(),
        request.description(),
        request.establishedDate()
    );

    return departmentMapper.toDto(department);
  }

  // 5. 부서 삭제
  @Override
  @Transactional
  public void delete(Long id) {

    Department department = findEntityById(id);

    // 직원 존재 여부 체크
    if (employeeRepository.existsByDepartmentId(id)) {
      throw new BusinessException(ErrorCode.DEPT_DELETE_NOT_ALLOWED);
    }

    departmentRepository.delete(department);
  }
}