package com.example.hrbank.domain.department.controller;

import com.example.hrbank.domain.department.controller.api.DepartmentApi;
import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.dto.request.DepartmentCreateRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentSearchRequest;
import com.example.hrbank.domain.department.dto.request.DepartmentUpdateRequest;
import com.example.hrbank.domain.department.dto.response.CursorPageResponseDepartmentDto;
import com.example.hrbank.domain.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
  public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentCreateRequest request) {
    DepartmentDto created = departmentService.create(request, 0);
    return ResponseEntity.ok(created);
  }

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseDepartmentDto> findAll(
      @ModelAttribute DepartmentSearchRequest request) {

    if (request == null) {
      request = new DepartmentSearchRequest(null, null, null, 10, null, null);
    }

    CursorPageResponseDepartmentDto result = departmentService.findAll(request);
    return ResponseEntity.ok(result);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> find(@PathVariable Long id) {
    DepartmentDto department = departmentService.find(id);
    return ResponseEntity.ok(department);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDto> update(
      @PathVariable Long id,
      @RequestBody DepartmentUpdateRequest request) {

    DepartmentDto updated = departmentService.update(id, request);
    return ResponseEntity.ok(updated);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    departmentService.delete(id);
    return ResponseEntity.noContent().build();
  }
}