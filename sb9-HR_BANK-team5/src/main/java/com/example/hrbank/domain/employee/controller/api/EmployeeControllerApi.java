package com.example.hrbank.domain.employee.controller.api;


import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDistributionDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeTrendDto;
import com.example.hrbank.domain.employee.dto.request.EmployeeCreateRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import com.example.hrbank.domain.employee.dto.request.StatRequest;
import com.example.hrbank.domain.employee.dto.request.StatsCountRequest;
import com.example.hrbank.domain.employee.dto.request.StatsDepartmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="직원 관리",description = "직원 관리 API")
public interface EmployeeControllerApi {

  @Operation(summary ="직원 등록",description = "새로운 직원을 등록합니다.",
  requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content=@Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
  ))
  @ApiResponses(value={
      @ApiResponse(responseCode = "200",description = "등록 성공",
      content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "400",description = "잘못된 요청 또는 중복된 이메일",
      content =@Content(examples = @ExampleObject(value = "Invalid request or Email already exists"))),
      @ApiResponse(responseCode = "404",description = "부서를 찾을 수 없음",
      content=@Content(examples = @ExampleObject(value = "Not found department"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content = @Content(examples = @ExampleObject(value = "Error server")))

  })
  public ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(description = "직원 생성 정보(JSON",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
       @RequestPart("employee") EmployeeCreateRequest employeeCreateRequest,
       @RequestPart(value="profile",required = false) MultipartFile profile,
      HttpServletRequest request
  );

  @Operation(summary = "직원 목록 조회",description = "직원 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",description = "조회 성공",
      content = @Content(schema = @Schema(implementation = CursorPageResponseEmployeeDto.class))),
      @ApiResponse(responseCode = "400",description = "잘못된 요청",
      content=@Content(examples = @ExampleObject("Wrong request"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content=@Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<CursorPageResponseEmployeeDto> searchEmployees(
      @ParameterObject EmployeeSearchRequest request
  );

  @Operation(summary = "직원 상세 조회",description = "직원 상세 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",description = "조회 성공",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
          content=@Content(examples = @ExampleObject("Not found Employee"))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content=@Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<EmployeeDto> searchEmployeeById(
      @Parameter(description = "직원 ID",required = true) Long Id);

  @Operation(summary = "직원 수정",description = "직원 정보를 수정합니다.",  requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content=@Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
  ))
  @ApiResponses(value ={
      @ApiResponse(responseCode = "200",description = "수정 성공",
      content= @Content(schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "400",description = "잘못된 요청 또는 중복된 이메일",
      content = @Content(examples = @ExampleObject("Invalid request or Email already exists"))),
      @ApiResponse(responseCode = "404",description = "직원 또는 부서를 찾을 없음",
      content=@Content(examples =@ExampleObject("Not found Employee or Department"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content = @Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<EmployeeDto> updateEmployee(
      @Parameter(description = "수정할 직원 ID", required = true) Long id,
      @Parameter(description = "수정 정보", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
      @RequestPart("employee") EmployeeUpdateRequest employeeUpdateRequest,
      @RequestPart(value="profile",required = false) MultipartFile profile,
      HttpServletRequest request
  );


  @Operation(summary = "직원 삭제", description = "직원을 삭제합니다")
  @ApiResponses(value ={
      @ApiResponse(responseCode = "204",description = "삭제 성공"),
      @ApiResponse(responseCode = "404",description = "직원을 찾을 수 없음",
      content=@Content(examples = @ExampleObject("Not found Employee"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content = @Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<Void> deleteEmployee(
      @Parameter(description = "직원 ID",required = true) Long Id,
      HttpServletRequest request
  );
  @Operation(summary = "직원 수 추이 조회",description = "지정된 기간 및 시간 단위로 그룹화된 직원 수 추이를 조회합니다. 파라미터를 제공하지 않으면 최근 12개월 데이터를 월 단위로 반환합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",description = "조회 성공",
      content = @Content(schema = @Schema(implementation = EmployeeTrendDto.class))),
      @ApiResponse(responseCode = "400",description = "잘못된 요청 또는 지원하지 않는 시간 단위",
      content=@Content(examples = @ExampleObject("Invalid request or unsupported time unit"))),
      @ApiResponse(responseCode ="500",description = "서버 오류",
      content=@Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<List<EmployeeTrendDto>> getStatsTrend(
    @ParameterObject StatRequest request
  );

  @Operation(summary = "직원 분포 조회",description = "지정된 기준으로 그룹화된 직원 분포를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",description = "조회 성공",
      content = @Content(schema = @Schema(implementation = EmployeeDistributionDto.class))),
      @ApiResponse(responseCode = "400",description = "잘못된 요청 또는 지원하지 않는 그룹화 기준",
      content = @Content(examples = @ExampleObject("Invalid request or unsupported grouping criteria"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content = @Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<List<EmployeeDistributionDto>> getStatsDistribution(
      @ParameterObject StatsDepartmentRequest request
  );

  @Operation(summary = "직원 수 조회", description = "지정된 조건에 맞는 직원 수를 조회합니다. 상태 필터링 및 입사일 기간 필터링이 가능합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",description = "조회 성공",
          content = @Content(schema = @Schema(implementation = Long.class))),
      @ApiResponse(responseCode = "400",description = "잘못된 요청",
      content = @Content(examples = @ExampleObject("Wrong request"))),
      @ApiResponse(responseCode = "500",description = "서버 오류",
      content=@Content(examples = @ExampleObject("Error server")))
  })
  public ResponseEntity<Long> countEmployee(
      @ParameterObject StatsCountRequest request
  );




}
