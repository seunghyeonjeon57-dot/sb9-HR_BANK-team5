package com.example.hrbank.domain.binarycontent.controller;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
import com.example.hrbank.infrastructure.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
  private final BinaryContentService binaryContentService;
  private final Storage storage;

  //find
@GetMapping()
  public ResponseEntity<BinaryContentDto> findById(
      @PathVariable("id") Long id
  ){
    BinaryContentDto binaryContentDto = binaryContentService.findById(id);
    return ResponseEntity.ok(binaryContentDto);
  }


  //download
  //delete

}
