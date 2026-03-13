package com.example.hrbank.domain.binarycontent.repository;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
  BinaryContent save(BinaryContent binaryContent);

}