package com.example.hrbank.domain.binarycontent.service.basic;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.dto.request.BinaryContentRequest;
import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.binarycontent.repository.BinaryContentRepository;
import com.example.hrbank.domain.binarycontent.service.BinaryContentService;
import com.example.hrbank.infrastructure.Storage;
import jakarta.transaction.Transactional;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository repository;
  private final Storage storage;

  @Override
  public ResponseEntity<Resource> download(Long id) {
    BinaryContent file = repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("없는 파일입니다"));

    BinaryContentDto dto = new BinaryContentDto(
        file.getId(),
        file.getFileName(),
        file.getContentType(),
        file.getFileSize(),
        file.getCreatedAt()
    );
    return storage.download(dto);
  }

  @Override
  @Transactional
  public BinaryContentDto save(BinaryContentRequest request, Path filePath) {

    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        request.contentType(),
        request.fileSize()
    );
    BinaryContent savedEntity = repository.save(binaryContent);
    storage.put(savedEntity.getId(),filePath);

    return new BinaryContentDto(
        savedEntity.getId(),
        savedEntity.getFileName(),
        savedEntity.getContentType(),
        savedEntity.getFileSize(),
        savedEntity.getCreatedAt()
    );
  }
  //path 추가
  //BinaryContentDto save(BinaryContentRequest request, java.nio.file.Path filePath);

  @Override
  @Transactional
  public BinaryContentDto findById(Long id) {
    BinaryContent entity = repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("없는 파일입니다"));
    return new BinaryContentDto(
        entity.getId(),
        entity.getFileName(),
        entity.getContentType(),
        entity.getFileSize(),
        entity.getCreatedAt()
    );

  }

  @Override
  @Transactional
  public void delete(Long id) {
    BinaryContent content = repository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("없는 파일 입니다"));

    storage.delete(id);
    repository.delete(content);
  }
}
