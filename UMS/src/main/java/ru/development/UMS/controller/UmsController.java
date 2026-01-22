package ru.development.UMS.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.development.UMS.dto.*;
import ru.development.UMS.service.RoleService;
import ru.development.UMS.service.UserService;
import ru.development.UMS.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.development.UMS.repository.UserRepository;

import java.util.*;

@RestController
@RequestMapping("ums")
@RequiredArgsConstructor
public class UmsController {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody User user) {
        User created = userService.register(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Авторизация и выдача JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    @PostMapping("/authorization")
    public ResponseEntity<AuthResponse> authorization(@RequestBody AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String username = auth.getName();
        Integer userId = userRepository.findByUsername(username)
                .map(u -> u.getId())
                .orElse(null);
        Set<String> roles = userRepository.getRolesUser(userId);
        String token = jwtService.generateToken(username, userId, roles);
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(summary = "Получение информации о пользователе по id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @GetMapping("/get/info_user/{id}")
    public ResponseEntity<User> getUserInfo(@PathVariable Integer id) {
        return userService.getById(id).map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Создание пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Создано",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.create(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Получение информации о всех пользователях", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Удаление пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Удален"),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.delete(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Получение всех ролей", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAllRoles() {
        List<String> roles;
        try {
            roles = roleService.getAllRoleNames();
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonList(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @Operation(summary = "Создание новой роли", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Создано",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @PostMapping("/roles")
    public ResponseEntity<String> createRole(@RequestBody RoleRequest req) {
        try {
            roleService.createRole(req.getRole(), req.getDescription());
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ROLE_CREATED", HttpStatus.CREATED);
    }

    @Operation(summary = "Назначение роли пользователю", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь/роль не найдены")
    })
    @PostMapping("/users/{id}/roles")
    public ResponseEntity<UserApiResponse> assignRole(@PathVariable Integer id, @RequestBody RoleRequest req) {
        User updated;
        try {
            updated = userService.assignRole(id, req.getRole());

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new UserApiResponse(null, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new UserApiResponse(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new UserApiResponse(updated, null), HttpStatus.OK);
    }
}
