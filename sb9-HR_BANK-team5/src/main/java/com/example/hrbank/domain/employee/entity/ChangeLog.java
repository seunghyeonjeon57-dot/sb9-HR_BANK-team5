package com.example.hrbank.domain.employee.entity;


import com.example.hrbank.domain.employee.entity.enums.ChangeLogType;
import com.example.hrbank.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="change_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangeLog extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name ="type",nullable = false)
  private ChangeLogType type;
  @Column(name= "employee_number",nullable = false,length = 100)
  private String employeeNumber;
  @Column(name="memo",nullable = false,length = 255)
  private String memo;
  @Column(name="ip_address",length = 255)
  private String ipAddress;
  @OneToMany(mappedBy = "changeLog", cascade = CascadeType.ALL,orphanRemoval = true)
  private List<ChangeLogDiff> diffs=new ArrayList<>();





  @Builder
  public ChangeLog(ChangeLogType type, String employeeNumber, String memo, String ipAddress
      ) {
    this.type = type;
    this.employeeNumber = employeeNumber;
    this.memo = memo;
    this.ipAddress = ipAddress;

  }
  public void addDiff(String propertyName, String before, String after) {
    ChangeLogDiff diff = new ChangeLogDiff(propertyName, before, after, this);
    this.diffs.add(diff);
  }
}
