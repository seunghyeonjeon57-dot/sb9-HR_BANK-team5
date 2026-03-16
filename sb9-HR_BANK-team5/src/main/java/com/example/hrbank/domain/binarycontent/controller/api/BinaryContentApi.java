package com.example.hrbank.domain.binarycontent.controller.api;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import org.springframework.core.io.Resource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "BinaryContent API")
public interface BinaryContentApi {
  //find
  @Operation(summary = "파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "파일 조회 성공",
          content = @Content(
              schema = @Schema(implementation = BinaryContentDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "없는 파일",
          content = @Content(
              examples = @ExampleObject(value = "{id} not found"))
      )
  })
  ResponseEntity<BinaryContentDto>findById(
      @Parameter(
          name = "id",
          description = "조회할 파일 ID",
          example = "1",
          required = true
      )
      Long id
  );


  //download
  @Operation(summary = "파일 다운")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "파일 다운 성공",
          content = @Content(
              mediaType = "application/octet-stream",
              schema = @Schema(type = "string", format = "binary")
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "없는 파일",
          content = @Content(
              examples = @ExampleObject(value = "{id} not found"))
      )
  })
  ResponseEntity<Resource>download(
      @Parameter(
          name = "id",
          description = "다운할 파일 ID",
          example = "1",
          required = true) Long id
  );

  //delete
  @Operation(summary = "파일 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "파일 삭제 성공"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "없는 파일",
          content = @Content(examples = @ExampleObject(value = "{id} not found"))
      )
  })
  ResponseEntity<Void> delete (
      @Parameter(
          name = "id",
          description = "삭제할 파일 ID",
          example = "1",
          required = true
      ) Long id
  );
}
