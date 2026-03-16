package com.example.hrbank.domain.binarycontent.controller;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class BinaryContentController {
  private final BinaryContentService binaryContentService;

@GetMapping("/{id}")
  public ResponseEntity<BinaryContentDto> findById(
      @PathVariable("id") Long id
  ){
    BinaryContentDto binaryContentDto = binaryContentService.findById(id);
    return ResponseEntity.ok(binaryContentDto);
  }

@GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
  BinaryContentDto dto = binaryContentService.findById(id);
{
  return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + dto.fileName() + "")
      .contentType(MediaType.parseMediaType(dto.contentType()))
      .body(binaryContentService.download(id).getBody());
}}

@DeleteMapping("{id}")
public ResponseEntity<Void> delete(
    @PathVariable("id") Long id
) {
  binaryContentService.delete(id);
  return ResponseEntity.noContent().build();
}

}
