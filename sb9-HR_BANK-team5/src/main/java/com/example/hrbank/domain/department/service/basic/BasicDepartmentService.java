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
import java.util.NoSuchElementException;
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
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseDepartmentDto findAll(DepartmentSearchRequest request) {
    int size = request.size() != null ? request.size() : 10;
    String sortField = request.sortField() != null ? request.sortField() : "establishedDate";
    String sortDirection = request.sortDirection() != null ? request.sortDirection().toLowerCase() : "asc";

    String lastValue = null;
    Long lastId = 0L;
    
    if (request.cursor() != null && !request.cursor().isBlank()) {
      try {
        byte[] decodedBytes = Base64.getDecoder().decode(request.cursor());
        Map<String, Object> map = objectMapper.readValue(new String(decodedBytes), Map.class);
        lastValue = map.get("v").toString();
        lastId = ((Number) map.get("id")).longValue();
      } catch (Exception e) {
        throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
      }
    }

    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField).and(Sort.by(Sort.Direction.ASC, "id"));
    Pageable pageable = PageRequest.of(0, size + 1, sort);

    List<Department> departments = departmentRepository.searchDepartments(
        request.nameOrDescription(), lastValue, lastId, sortField, sortDirection, pageable
    );

    boolean hasNext = departments.size() > size;
    if (hasNext) {
      departments = departments.subList(0, size);
    }
    return departmentMapper.toCursorPageDto(departments, size, departmentRepository.count(), hasNext, sortField);
  }


  
  @Override @Transactional public DepartmentDto create(DepartmentCreateRequest r, Integer c) {
    if (departmentRepository.existsByName(r.name())) throw new IllegalArgumentException("부서이름이 이미 존재합니다.");
    Department d = new Department(r.name(), r.description(), r.establishedDate(), c);
    return departmentMapper.toDto(departmentRepository.save(d));
  }
  @Override @Transactional(readOnly = true) public Department findEntityById(Long id) { return departmentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 Id를 가진 부서가 존재하지 않습니다.")); }
  @Override @Transactional(readOnly = true) public DepartmentDto find(Long id) { return departmentMapper.toDto(findEntityById(id)); }
  @Override @Transactional public DepartmentDto update(Long id, DepartmentUpdateRequest r) {
    Department d = findEntityById(id);
    if (!d.getName().equals(r.name()) && departmentRepository.existsByName(r.name())) {
      throw new IllegalArgumentException("부서 이름이 이미 존재합니다.");
    }

    d.update(r.name(), r.description(), r.establishedDate());
    return departmentMapper.toDto(d); }
  @Override @Transactional public void delete(Long id) { if (employeeRepository.existsByDepartmentId(id)) throw new IllegalStateException("소속 직원이 있는 부서는 삭제할 수 없습니다."); departmentRepository.delete(findEntityById(id)); }
}