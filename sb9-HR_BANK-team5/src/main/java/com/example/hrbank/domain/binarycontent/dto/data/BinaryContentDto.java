package com.example.hrbank.domain.binarycontent.dto.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;


@Builder
@Schema(description = "file DTO")
public record BinaryContentDto(
    Long id,
    String fileName,
    String contentType,
    Long fileSize,

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

}