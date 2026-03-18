package com.example.hrbank.domain.employee.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeLogDiff {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name="propertyName",nullable = false)
  private String propertyName;
  @Column(name="before",nullable = false)
  private String before;
  @Column(name="after",nullable = false)
  private String after;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="change_log_id")
  private ChangeLog changeLog;

  public ChangeLogDiff(String propertyName, String before, String after, ChangeLog changeLog) {
    this.propertyName = propertyName;
    this.before = before;
    this.after = after;
    this.changeLog = changeLog;
  }
}
