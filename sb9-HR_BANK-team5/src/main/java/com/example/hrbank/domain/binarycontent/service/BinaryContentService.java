package com.example.hrbank.domain.binarycontent.service;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.dto.request.BinaryContentRequest;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  ResponseEntity<Resource> download(Long id);

  BinaryContentDto save(BinaryContentRequest request, Path filePath);

  BinaryContentDto findById(Long id);

  void delete(Long id);
}
