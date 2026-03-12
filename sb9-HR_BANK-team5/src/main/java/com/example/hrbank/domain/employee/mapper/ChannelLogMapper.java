package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelLogMapper {
  ChangeLogDto toDto(ChangeLog changeLog);

}
