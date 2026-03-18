package com.example.hrbank.domain.employee.controller.api;


import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseChangeLogDto;
import com.example.hrbank.domain.employee.dto.request.ChangeLogCountRequest;
import com.example.hrbank.domain.employee.dto.request.ChangeLogSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.apache.catalina.LifecycleState;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name= "직원 정보 수정 이력 관리",description = "직원 정보 수정 이력 관리 API")
public interface ChangeLogControllerApi {
  @Operation(summary = "직원 정보 수정 이력 목록 조회",description = "직원 정보 수정 이력 목록을 조회합니다. 상세 변경 내용은 포함되지 않습니다. ")
  @ApiResponses(value={
      @ApiResponse(responseCode = "200",description = "조회 성공",
      content = @Content(schema = @Schema(implementation = CursorPageResponseChangeLogDto.class))),
      @ApiResponse(responseCode = "400",description = "잘못된 요청 또는 지원하지 않는 정렬 필드",
      content = @Content(examples = @ExampleObject("Wrong request or unsupported sort field"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content = @Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<CursorPageResponseChangeLogDto> getChangeLogs(
    @ParameterObject ChangeLogSearchRequest request
  );
  @Operation(summary = "직원 정보 수정 이력 상세 조회",description = "직원 정보 수정 이력의 상세 정보를 조회합니다. 변경 상세 내용이 포함됩니다.")
  @ApiResponses(value={
      @ApiResponse(responseCode = "200",description = "조회 성공",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChangeLogDetailDto.class)))),
      @ApiResponse(responseCode = "404",description = "이력을 찾을 수 없음",
      content = @Content(examples = @ExampleObject("Change log not found"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content=@Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<ChangeLogDetailDto> getChangeLogsById(
      @Parameter(description = "이력 ID") @PathVariable Long id
  );
  @Operation(summary = "수정 이력 건수 조회",description = "직원 정보 수정 이력 건수를 조회합니다. 파라미터를 제공하지 않으면 최근 일주일 데이터를 반환합니다.")
  @ApiResponses(value={
      @ApiResponse(responseCode = "200",description = "조회 성공"),
      @ApiResponse(responseCode = "400",description = "잘못된 요청 또는 유효하지 않은 날짜 범위",
      content=@Content(examples = @ExampleObject("Invalid request or invalid date range"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content=@Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<Long> getChangeLogsCount(
    @ParameterObject ChangeLogCountRequest request
  );

}
