package com.example.hrbank.domain.backup.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursorPageResponseBackupDto {

  private List<BackupDto> content;
  private String nextCursor;
  private Long nextIdAfter;
  private Integer size;
  private Long totalElements;
  private Boolean hasNext;

  public static CursorPageResponseBackupDto of(List<BackupDto> content, Long nextIdAfter, int size, long totalElements, boolean hasNext) {
    return CursorPageResponseBackupDto.builder()
        .content(content)
        .nextIdAfter(nextIdAfter)
        .size(size)
        .totalElements(totalElements)
        .hasNext(hasNext)
        .build();
  }
}