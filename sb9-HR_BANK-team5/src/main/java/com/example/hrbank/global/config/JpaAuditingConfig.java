package com.example.hrbank.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA Auditing 기능을 켭니다 (생성/수정시간 자동화)
//BaseTimeEntity작동용
public class JpaAuditingConfig {
}