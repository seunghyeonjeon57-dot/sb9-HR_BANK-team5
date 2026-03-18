package com.example.hrbank.domain.department.controller.api;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentUpdateRequest;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Department", description = "Department API")
public interface DepartmentApi {

  @Operation(summary = "부서 등록", operationId = "createDepartment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "등록 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이름",
          content = @Content(examples = @ExampleObject(value = "Department with name {name} already exists"))),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<?> create(@RequestBody DepartmentCreateRequest request);

  @Operation(summary = "부서 목록 조회", operationId = "getDepartments")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseDepartmentDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(examples = @ExampleObject(value = "Invalid search parameters"))),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<?> findAll(DepartmentSearchRequest request);

  @Operation(summary = "부서 상세 조회", operationId = "getDepartment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Department with id {id} not found"))),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<?> find(@Parameter(description = "조회할 부서 ID") @PathVariable Long id);

  @Operation(summary = "부서 수정", operationId = "updateDepartment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이름",
          content = @Content(examples = @ExampleObject(value = "Department with name {name} already exists"))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Department with id {id} not found"))),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<?> update(@PathVariable Long id, @RequestBody DepartmentUpdateRequest request);

  @Operation(summary = "부서 삭제", operationId = "deleteDepartment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "400", description = "소속 직원이 있는 부서는 삭제할 수 없음"),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Department with id {id} not found"))),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<?> delete(@PathVariable Long id);
}