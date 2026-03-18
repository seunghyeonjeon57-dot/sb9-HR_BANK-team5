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

  default CursorPageResponseDepartmentDto toCursorPageDto(List<Department> entities, Integer size, Long totalElements, boolean hasNext, String sortField) {
    List<DepartmentDto> content = toDtoList(entities);
    String encodedCursor = null;

    if (!entities.isEmpty()) {
      Department lastItem = entities.get(entities.size() - 1);

      // 정렬 필드에 맞는 값을 추출하여 커서에 포함
      String lastValue = switch (sortField) {
        case "name" -> lastItem.getName();
        case "establishedDate" -> lastItem.getEstablishedDate().toString();
        default -> lastItem.getId().toString();
      };

      String jsonCursor = String.format("{\"v\":\"%s\",\"id\":%d}", lastValue, lastItem.getId());
      encodedCursor = Base64.getEncoder().encodeToString(jsonCursor.getBytes());
    }

    Long nextIdAfter = entities.isEmpty() ? null : entities.get(entities.size()-1).getId();

    return new CursorPageResponseDepartmentDto(content, encodedCursor, nextIdAfter, size, totalElements, hasNext);
  }
}