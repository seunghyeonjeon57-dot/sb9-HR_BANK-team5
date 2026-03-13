package com.example.hrbank.infrastructure;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

@Component
public class LocalStorage implements Storage {

  private final Path root;

  public LocalStorage(
      @Value("${./data/storage}") Path root
  ) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectories(root);
      }
    } catch (IOException e) {
      throw new RuntimeException("저장소 초기화 실패", e);
    }
  }

  @Override
  public Long put(Long id, Path filePath) {
    Path targetPath = resolvePath(id);
    try {
      Files.copy(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
      return id;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패");
    }
  }

  @Override
  public InputStream get(Long id) {
    Path filePath = resolvePath(id);
    if (Files.notExists(filePath)) {
      throw new NoSuchElementException("파일 없음: " + id);
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException("파일 읽기 실패", e);
    }
  }

  @Override
  public void delete(Long id) {
    Path filePath = resolvePath(id);
    try {
      if (Files.exists(filePath)) {
        Files.delete(filePath);
      }
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 실패 / ID: " + id, e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto metaData) {
    InputStreamResource resource = new InputStreamResource(get(metaData.id()));
    String encodedFileName = UriUtils.encode(metaData.fileName(), StandardCharsets.UTF_8);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
        .contentType(MediaType.parseMediaType(metaData.contentType()))
        .body(resource);
  }

  private Path resolvePath(Long id) {
    return root.resolve(id.toString());
  }
}