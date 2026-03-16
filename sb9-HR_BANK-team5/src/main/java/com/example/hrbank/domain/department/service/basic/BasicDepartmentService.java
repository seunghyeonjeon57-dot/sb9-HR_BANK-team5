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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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

  @Transactional
  @Override
  public DepartmentDto create(DepartmentCreateRequest departmentCreateRequest, Integer employeeCount) {
    String name = departmentCreateRequest.name();
    String description = departmentCreateRequest.description();
    LocalDate establishedDate = departmentCreateRequest.establishedDate();

    if(departmentRepository.existsByName(name))
      throw new IllegalArgumentException("Departmentname with email " + name + " already exists");

    Department department = new Department(name, description, establishedDate, employeeCount);
    departmentRepository.save(department);

    return departmentMapper.toDto(department);
  }

  @Override
  @Transactional(readOnly = true)
  public Department findEntityById(Long id) {
    return departmentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Department with id " + id + " not found"));
  }


  //부서 상세 조회
  @Override
  @Transactional(readOnly = true)
  public DepartmentDto find(Long id) {

    Department department = findEntityById(id);

    return departmentMapper.toDto(department);
  }

  //부서 목록 조회
  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseDepartmentDto findAll(DepartmentSearchRequest request) {

//  한 페이지에 보여줄 개수
    int size = request.size() != null ? request.size() : 10;
//  cursor 기반 idAfter
    Long idAfter = 0L;
    if (request.cursor() != null && !request.cursor().isBlank()) {
      try {
        byte[] decodedBytes = Base64.getDecoder().decode(request.cursor());
        String json = new String(decodedBytes);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(json, Map.class);
        idAfter = ((Number) map.get("id")).longValue();
      } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
        // cursor 파싱 실패 시 기본값 0 사용
        idAfter = 0L;
      }
    } else if (request.idAfter() != null) {
      idAfter = request.idAfter();
    }
    // 정렬 필드 기본값
    String sortField = request.sortField() != null ? request.sortField() : "establishedDate";

    //허용된 정렬 필드 검증
    if (!List.of("name", "establishedDate").contains(sortField)) {
      sortField = "establishedDate";
    }

// 정렬 방향 기본값
    String sortDirection = request.sortDirection() != null ? request.sortDirection() : "asc";

// Sort 객체 생성
    Sort sort = Sort.by(
        Sort.Direction.fromString(sortDirection),
        sortField
    );

//    한 페이지에 3개를 보여줄려고 size = 3을 하고, db에서는 +1을 한 4를 조회한다.
//    그럼 3개 이상이 있다는 뜻이니까 3개만 조회했을 경우 다음 페이지가 있다는 것을 알 수 있다.
    Pageable pageable = PageRequest.of(0, size + 1, sort);

//    idAfter = 5 , size = 3 이면, 조회 결과(departments) : 6,7,8,9
    List<Department> departments =
        departmentRepository.searchDepartments(
            request.nameOrDescription(),
            idAfter,
            pageable
        );
//    다음 페이지 존재 여부 판단. size = 3, 조회 = 4 이면 다음 페이지가 있음
    boolean hasNext = departments.size() > size;

//    초과 데이터 제거. 저 위에서 말한 부분. 6,7,8,9 중 실제로는 6,7,8만 반환
    if (hasNext) {
      departments = departments.subList(0, size);
    }
//    전체 부서 개수 조회
    long totalElements = departmentRepository.count();

    return departmentMapper.toCursorPageDto(
        departments,
        size,
        totalElements,
        hasNext
    );
  }

  @Override
  @Transactional
  public DepartmentDto update(Long id, DepartmentUpdateRequest request) {

    Department department = findEntityById(id);

    if (!department.getName().equals(request.name())
        && departmentRepository.existsByName(request.name())) {
      throw new IllegalArgumentException("Department name already exists");
    }

    department.update(
        request.name(),
        request.description(),
        request.establishedDate()
    );

    return departmentMapper.toDto(department);
  }


  @Override
  @Transactional
  public void delete(Long id) {

    Department department = findEntityById(id);

    departmentRepository.delete(department);
  }
}
