package com.example.hrbank.domain.department.controller;

import com.example.hrbank.domain.department.controller.api.DepartmentApi;
import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentUpdateRequest;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import com.example.hrbank.domain.department.service.DepartmentService;
import com.example.hrbank.global.error.ErrorResponse;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController implements DepartmentApi {

  private final DepartmentService departmentService;

  @Override
  @PostMapping
//  ResponseEntity<?> 이렇게 하면 정상 응답은 DepartmentDto, 예외 응답은 ErrorResponse**가 되어도 타입 충돌이 없음
  public ResponseEntity<?> create(@RequestBody DepartmentCreateRequest request) {
    try {
      DepartmentDto created = departmentService.create(request, 0);
      return ResponseEntity.ok(created);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ErrorResponse.of(400, "잘못된 요청입니다.", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ErrorResponse.of(500, "서버 오류", e.getMessage()));
    }
  }

  @Override
  @GetMapping
  public ResponseEntity<?> findAll(@RequestBody(required = false) DepartmentSearchRequest request) {
    try {
      if (request == null) request = new DepartmentSearchRequest(null, null, null, 10, null, null);
      CursorPageResponseDepartmentDto result = departmentService.findAll(request);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ErrorResponse.of(400, "잘못된 요청입니다.", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ErrorResponse.of(500, "서버 오류", e.getMessage()));
    }
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<?> find(@PathVariable Long id) {
    try {
      DepartmentDto department = departmentService.find(id);
      return ResponseEntity.ok(department);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ErrorResponse.of(404, "부서를 찾을 수 없음", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ErrorResponse.of(500, "서버 오류", e.getMessage()));
    }
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DepartmentUpdateRequest request) {
    try {
      DepartmentDto updated = departmentService.update(id, request);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ErrorResponse.of(400, "잘못된 요청 또는 중복된 이름", e.getMessage()));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ErrorResponse.of(404, "부서를 찾을 수 없음", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ErrorResponse.of(500, "서버 오류", e.getMessage()));
    }
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
      departmentService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalStateException e) { // 소속 직원이 있는 경우
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ErrorResponse.of(400, "소속 직원이 있는 부서는 삭제할 수 없음", e.getMessage()));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ErrorResponse.of(404, "부서를 찾을 수 없음", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ErrorResponse.of(500, "서버 오류", e.getMessage()));
    }
  }
}