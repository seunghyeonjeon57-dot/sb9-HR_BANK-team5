package com.example.hrbank.domain.employee.entity;


import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import com.example.hrbank.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;
import org.springframework.cglib.core.Local;

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
  private ChannelType type;
  @Column(name= "employee_number",nullable = false,length = 100)
  private String employeeNumber;
  @Column(name="memo",nullable = false,length = 255)
  private String memo;
  @Column(name="ip_address",length = 255)
  private String ipAddress;
  @OneToMany(mappedBy = "changeLog", cascade = CascadeType.ALL,orphanRemoval = true)
  private List<ChannelDiff> diffs=new ArrayList<>();





  @Builder
  public ChangeLog(ChannelType type, String employeeNumber, String memo, String ipAddress
      ) {
    this.type = type;
    this.employeeNumber = employeeNumber;
    this.memo = memo;
    this.ipAddress = ipAddress;

  }
  public void addDiff(String propertyName, String before, String after) {
    ChannelDiff diff = new ChannelDiff(propertyName, before, after, this);
    this.diffs.add(diff);
  }
}
