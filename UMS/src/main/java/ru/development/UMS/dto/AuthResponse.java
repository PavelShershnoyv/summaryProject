package ru.development.UMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AuthResponse", description = "Ответ авторизации")
public class AuthResponse {
    @Schema(description = "JWT токен")
    private String token;
}
