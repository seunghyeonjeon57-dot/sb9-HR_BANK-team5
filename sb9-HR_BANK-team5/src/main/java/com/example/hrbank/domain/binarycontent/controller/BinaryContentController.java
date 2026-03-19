package com.example.hrbank.domain.binarycontent.controller;

import com.example.hrbank.domain.binarycontent.controller.api.BinaryContentApi;
import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "파일 관리", description = "파일 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class BinaryContentController implements BinaryContentApi {
  private final BinaryContentService binaryContentService;









@GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
  BinaryContentDto dto = binaryContentService.findById(id);
{
  return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + dto.fileName() + "")
      .contentType(MediaType.parseMediaType(dto.contentType()))
      .body(binaryContentService.download(id).getBody());
}}









}
