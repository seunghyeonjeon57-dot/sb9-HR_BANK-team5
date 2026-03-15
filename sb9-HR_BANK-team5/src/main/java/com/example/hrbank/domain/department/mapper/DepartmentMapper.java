package com.example.hrbank.domain.department.mapper;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import com.example.hrbank.domain.department.entity.Department;
import java.util.Base64;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

  DepartmentDto toDto(Department department);
  List<DepartmentDto> toDtoList(List<Department> entities);
  default CursorPageResponseDepartmentDto toCursorPageDto(List<Department> entities, Integer size, Long totalElements, boolean hasNext) {
    List<DepartmentDto> content = toDtoList(entities);
    Long lastId = entities.isEmpty() ? null : entities.get(entities.size()-1).getId();
    String encodedCursor = null;
    if (lastId != null) {
      encodedCursor = Base64.getEncoder().encodeToString(String.valueOf(lastId).getBytes());
    }


    return new CursorPageResponseDepartmentDto(content, encodedCursor,lastId, size, totalElements, hasNext);
  }
}
