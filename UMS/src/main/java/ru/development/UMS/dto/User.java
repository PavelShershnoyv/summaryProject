package ru.development.UMS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Schema(name = "User", description = "Пользователь системы")
@Data
public class User {
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Integer id;

    @Schema(description = "Имя пользователя (username)", example = "ivan.petrov")
    private String username;

    @Schema(description = "Email", example = "ivan.petrov@example.com")
    private String email;

    @Schema(description = "Дата регистрации (epoch seconds)", example = "1737300000")
    private Integer date;

    @Schema(description = "Пароль (только при создании)", example = "secret", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия", example = "Петров")
    private String lastName;

    @Schema(description = "Роли пользователя", example = "[\"ADMIN\",\"VIEWER\"]")
    private List<String> roles;
}
