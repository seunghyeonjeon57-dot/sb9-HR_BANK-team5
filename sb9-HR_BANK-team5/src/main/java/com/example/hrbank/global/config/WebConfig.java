package com.example.hrbank.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // "누가 /static/으로 시작하는 요청을 하면" -> "static 폴더 바로 아래서 찾아라"
    registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");
  }
}