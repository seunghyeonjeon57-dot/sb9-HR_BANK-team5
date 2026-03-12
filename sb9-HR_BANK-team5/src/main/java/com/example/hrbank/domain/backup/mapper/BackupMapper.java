package com.example.hrbank.domain.backup.mapper;

import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse;
import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse;
import com.example.hrbank.domain.backup.entity.BackupHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BackupMapper {

  BackupResponse toResponse(BackupHistory entity);
  List<BackupResponse> toResponseList(List<BackupHistory> entities);

  @Mapping(target = "content", source = "entities")
  BackupCursorPageResponse toPageResponse(
      List<BackupHistory> entities,
      String nextCursor,
      Long nextIdAfter,
      int size,
      long totalElements,
      boolean hasNext
  );
}