package com.example.hrbank.domain.employee.entity;


import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="change_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangeLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name ="type",nullable = false)
  private String type;
  @Column(name= "employee_number",nullable = false,length = 100)
  private String employeeNumber;
  @Column(name="memo",nullable = false,length = 255)
  private String memo;
  @Column(name="ip_address",length = 255)
  private String ipAddress;
  @Column(name="at",nullable = false)
  private Instant at;

  public ChangeLog(Long id, String type, String employeeNumber, String memo, String ipAddress,
      Instant at) {
    this.id = id;
    this.type = type;
    this.employeeNumber = employeeNumber;
    this.memo = memo;
    this.ipAddress = ipAddress;
    this.at = at;
  }
}
