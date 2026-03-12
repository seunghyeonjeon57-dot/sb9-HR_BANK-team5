package com.example.hrbank.domain.department.entity;

import com.example.hrbank.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
public class Department extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 50, nullable = false, unique = true)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "established_date", nullable = false)
  private LocalDate establishedDate;

  @Column(name = "employee_count", nullable = false)
  private Integer employeeCount;

  public Department(String name, String description, LocalDate establishedDate , Integer employeeCount ){
    this.name = name;
    this.description = description;
    this.establishedDate = establishedDate;
    this.employeeCount = employeeCount;
  }

}
