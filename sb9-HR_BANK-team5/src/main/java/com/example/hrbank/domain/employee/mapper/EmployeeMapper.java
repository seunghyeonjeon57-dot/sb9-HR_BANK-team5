package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.binarycontent.mapper.BinaryContentMapper;
import com.example.hrbank.domain.department.mapper.DepartmentMapper;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.entity.Employee;
import java.util.Base64;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={BinaryContentMapper.class, DepartmentMapper.class})
public interface EmployeeMapper {
  @Mapping(source="department.id", target = "departmentId")
  @Mapping(source = "profileImage.id", target="profileImageId")
  @Mapping(source="department.name", target="departmentName")
  EmployeeDto toDto(Employee employee);

  List<EmployeeDto> toDtoList(List<Employee> entities);

  default CursorPageResponseEmployeeDto toCursorPageResponse(
      List<Employee> entities,
      long totalElements,
      int size,
      boolean hasNext,
      String sortField
  ){
    List<EmployeeDto> content = toDtoList(entities);
    String encodedCursor = null;

    if(!entities.isEmpty()){
      Employee lastItem = entities.get(entities.size() - 1);

      // 정렬 필드에 따른 커서 값 추출
      String lastValue = switch (sortField != null ? sortField : "id") {
        case "name" -> lastItem.getName();
        case "employeeNumber" -> lastItem.getEmployeeNumber();
        case "hireDate" -> lastItem.getHireDate().toString();
        default -> lastItem.getId().toString();
      };

      // 복합 커서 생성 (JSON 기반)
      String jsonCursor = String.format("{\"v\":\"%s\",\"id\":%d}", lastValue, lastItem.getId());
      encodedCursor = Base64.getEncoder().encodeToString(jsonCursor.getBytes());
    }

    Long nextIdAfter = entities.isEmpty() ? null : entities.get(entities.size()-1).getId();

    return new CursorPageResponseEmployeeDto(
        content,
        encodedCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext
    );
  }
}