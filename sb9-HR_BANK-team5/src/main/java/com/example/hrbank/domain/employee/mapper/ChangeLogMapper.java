package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import java.util.Base64;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {
  @Mapping(source = "createdAt", target = "at")
  ChangeLogDto toDto(ChangeLog changeLog);

  List<ChangeLogDto> toDtoList(List<ChangeLog> entities);

  default CursorPageResponseChangeLogDto CursorPageResponse(
      List<ChangeLog> entities,
      ChannelType type,
      Integer size,
      Long totalElements,
      boolean hasNext

  ) {
    List<ChangeLogDto> content = toDtoList(entities);
    Long lastId = entities.isEmpty() ? null : entities.get(entities.size() - 1).getId();
    String encodedCursor = null;
    if (lastId != null) {
      encodedCursor = Base64.getEncoder().encodeToString(String.valueOf(lastId).getBytes());
    }
      return new CursorPageResponseChangeLogDto(
          content,
          type,
          encodedCursor,
          lastId,
          size,
          totalElements,
          hasNext
      );

    }

  @Mapping(source = "entity.id", target = "id")
  @Mapping(source = "entity.employeeNumber", target = "employeeNumber")
  @Mapping(source = "employee.profileImage.id", target = "profileImageId")
  @Mapping(source = "employee.name", target = "employeeName")
  @Mapping(source = "entity.memo", target = "memo")
  @Mapping(source = "entity.type", target = "type")
  @Mapping(source = "entity.createdAt", target = "at")
    ChangeLogDetailDto toDetailDto(ChangeLog entity, Employee employee);



}
