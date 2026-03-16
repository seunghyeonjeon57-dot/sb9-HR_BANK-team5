package com.example.hrbank.domain.employee.entity;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.example.hrbank.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@Entity(name = "employees")
@Getter
public class Employee extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name ="name",length = 50,nullable = false)
  private String name;
  @Column(name = "email",length=100,nullable = false,unique = true)
  private String email;
  @Column(name="employee_number",length =50,nullable = false,unique = true)
  private String employeeNumber;
  @Column(name="position",length = 50)
  private String position;
  @Column(name="hire_date",nullable = false)
  private LocalDate hireDate;
  @Column(name="resignation_date")
  private LocalDate resignationDate;

  @Enumerated(EnumType.STRING)
  @Column(name="status",nullable = false)
  private EmployeeStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="department_id")
  private Department department;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="profile_image_id")
  private BinaryContent profileImage;


  public Employee(Long id, String name, String email, String employeeNumber, String position,
      LocalDate hireDate,LocalDate resignationDate, EmployeeStatus status, Department department,
      BinaryContent profileImage) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.employeeNumber = employeeNumber;
    this.position = position;
    this.hireDate = hireDate;
    this.resignationDate=resignationDate;
    this.status = status;
    this.department = department;
    this.profileImage = profileImage;
  }

  public void changeDepartment(Department newdepartment){
    this.department=newdepartment;
  }
  public void updateEmployee(String newName,String newEmail,String newPosition,LocalDate newHireDate,EmployeeStatus newStatus){
    this.name =newName;
    this.email =newEmail;
    this.position=newPosition;
    this.hireDate=newHireDate;
    this.status = newStatus;
  }
  public void resign(){
    this.status=EmployeeStatus.RESIGNED;
    this.resignationDate = LocalDate.now();

  }
}
