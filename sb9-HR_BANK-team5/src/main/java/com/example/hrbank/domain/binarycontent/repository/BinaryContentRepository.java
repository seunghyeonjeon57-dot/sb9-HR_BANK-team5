package com.example.hrbank.domain.binarycontent.repository;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
  Optional<BinaryContent> findById(Long Id);
  List<BinaryContent> findAllById(Long Id);

  BinaryContent save(BinaryContent binaryContent);

}