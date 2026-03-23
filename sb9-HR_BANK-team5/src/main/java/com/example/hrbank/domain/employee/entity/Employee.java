package com.example.hrbank.domain.employee.entity;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.example.hrbank.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.websocket.Decoder.Binary;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 엔티티는 거의 NoArgsConstructor
@Table
@Entity(name = "employees") // 테이블 명 정의
@Getter
public class Employee extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name", length = 50, nullable = false)
  private String name;
  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;
  @Column(name = "employee_number", length = 50, nullable = false, unique = true)
  private String employeeNumber;
  @Column(name = "position", length = 50)
  private String position;
  @Column(name = "hire_date", nullable = false)
  private LocalDate hireDate;
  @Column(name = "resignation_date")
  private LocalDate resignationDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private EmployeeStatus status;

  @ManyToOne(fetch = FetchType.LAZY) // 사원 정보를 가져올때 당장 필요없는 부서 정보까지 가져와서 서버를 무겁지 않게.
  @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "FK_EMP_DEPT"))
  private Department department;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_image_id", foreignKey = @ForeignKey(name = "FK_EMP_PROFILE"))
  private BinaryContent profileImage;

  @Builder //
  public Employee(Long id, String name, String email, String employeeNumber, String position,
      LocalDate hireDate, LocalDate resignationDate, EmployeeStatus status, Department department,
      BinaryContent profileImage) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.employeeNumber = employeeNumber;
    this.position = position;
    this.hireDate = hireDate;
    this.resignationDate = resignationDate;
    this.status = status;
    this.department = department;
    this.profileImage = profileImage;
  }

  public void changeDepartment(Department newdepartment) {
    this.department = newdepartment;
  }

  public void updateEmployee(String newName, String newEmail, String newPosition,
      LocalDate newHireDate, EmployeeStatus newStatus,
      BinaryContent newProfileImage) {
    this.name = newName;
    this.email = newEmail;
    this.position = newPosition;
    this.hireDate = newHireDate;
    this.status = newStatus;
    this.profileImage = newProfileImage;
  }

  public void resign() {
    this.status = EmployeeStatus.RESIGNED;
    this.resignationDate = LocalDate.now();

  }
}

//메서드가잇는이유?:엔티티에 비즈니스 로직(update, resign 등)을 포함시켜 객체가 스스로의 상태를 관리하게 했습니다. 이를 통해 서비스 계층의 로직을 단순화하고, 데이터의 일관성을 엔티티 내부에서 강제할 수 있도록 설계했습니다