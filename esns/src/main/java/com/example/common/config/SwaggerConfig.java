package com.example.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(title = "감정 SNS API", version = "v1"),
  security = @SecurityRequirement(name = "bearerAuth")         // 전역 적용
)
@SecurityScheme(
  name = "bearerAuth",                                         // 위 securityRequirement와 동일한 이름
  type = SecuritySchemeType.HTTP,
  scheme = "bearer",
  bearerFormat = "JWT",
  in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    // (아무 메서드도 필요 없습니다)
}
