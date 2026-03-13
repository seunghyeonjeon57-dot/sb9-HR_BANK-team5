package com.example.hrbank.infrastructure;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import java.io.InputStream;
import org.springframework.http.ResponseEntity;

public interface storage {
  Long put(Long Id, byte[] bytes);

  InputStream get(Long Id);

  ResponseEntity<?> download(BinaryContentDto metaData);

  void delete(Long id);

}
