package com.example.hrbank.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 테이블로 생성되지 않고, 상속받는 엔티티들에게 매핑 정보만 제공 기초 클래스)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt; // 생성 일시

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt; // 수정 일시
}