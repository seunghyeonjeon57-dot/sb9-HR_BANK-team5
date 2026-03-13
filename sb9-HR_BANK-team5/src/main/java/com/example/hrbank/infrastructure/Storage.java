package com.example.hrbank.infrastructure;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import org.springframework.http.ResponseEntity;

public interface Storage {
  Long put(Long id, byte[] bytes);

  InputStream get(Long id);

  ResponseEntity<Resource> download(BinaryContentDto metaData);

  void delete(Long id);

}
