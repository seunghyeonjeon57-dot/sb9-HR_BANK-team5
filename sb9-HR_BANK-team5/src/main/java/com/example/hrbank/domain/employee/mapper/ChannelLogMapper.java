package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelLogMapper {
  ChangeLogDto toDto(ChangeLog changeLog);

  List<ChangeLogDto> toDtoList(List<ChangeLog> changeLogs);




}
