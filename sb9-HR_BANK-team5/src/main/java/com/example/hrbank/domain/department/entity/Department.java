package com.example.hrbank.domain.department.entity;

import com.example.hrbank.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
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


  @Builder
  public Department(String name, String description, LocalDate establishedDate , Integer employeeCount ){
    this.name = name;
    this.description = description;
    this.establishedDate = establishedDate;
    this.employeeCount = employeeCount;
  }

  public void addEmployee() {
    this.employeeCount++;
  }

  public void removeEmployee() {
    if (this.employeeCount > 0) {
      this.employeeCount--;
    }
  }

//  public static Type builder() {
//    return null;
//  }

  public void update(String name, String description, LocalDate establishedDate) {
    this.name = name;
    this.description = description;
    this.establishedDate = establishedDate;
  }

}
