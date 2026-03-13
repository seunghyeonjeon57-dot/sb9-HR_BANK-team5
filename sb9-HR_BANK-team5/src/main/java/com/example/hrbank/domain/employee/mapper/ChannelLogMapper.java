package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import java.util.Base64;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelLogMapper {

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



}
