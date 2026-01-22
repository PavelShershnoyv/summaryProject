package ru.development.MSS.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.development.MSS.dto.Message;
import ru.development.MSS.dto.SubscriptionRequest;
import ru.development.MSS.service.MssService;
import ru.development.MSS.security.JwtService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("mss")
@RequiredArgsConstructor
public class MssController {
    private final MssService mssService;
    private final JwtService jwtService;

    @PostMapping("/messages")
    @Operation(summary = "Создание сообщения", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Создано",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message created = mssService.create(message);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/messages/{id}")
    @Operation(summary = "Получение сообщения по id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    public ResponseEntity<Message> getMessageById(@PathVariable Integer id) {
        return mssService.getById(id)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/messages")
    @Operation(summary = "Получение всех сообщений", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)))
    })
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = mssService.getAll();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/messages/producer/{producerId}")
    @Operation(summary = "Получение сообщений продюсера", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)))
    })
    public ResponseEntity<List<Message>> getMessagesByProducer(@PathVariable Integer producerId) {
        List<Message> messages = mssService.getByProducer(producerId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/messages/subscriber/{subscriberId}")
    @Operation(summary = "Получение сообщений подписчика", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)))
    })
    public ResponseEntity<List<Message>> getMessagesBySubscriber(@PathVariable Integer subscriberId) {
        List<Message> messages = mssService.getBySubscriber(subscriberId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/messages/{id}")
    @Operation(summary = "Удаление сообщения", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Удалено"),
            @ApiResponse(responseCode = "404", description = "Не найдено")
    })
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Message> message = mssService.getById(id);

        if (message.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        Set<String> roles = jwtService.getRoles(token);
        if (roles.contains("ADMIN")) {
            mssService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Integer uid = jwtService.getUserId(token);
        Integer producerId = message.get().getProducerId();
        if (producerId == null || !java.util.Objects.equals(producerId, uid)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        mssService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Подписка на пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Создано"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    public ResponseEntity<Void> subscribe(@RequestBody SubscriptionRequest request) {
        mssService.subscribe(request.getSubscriberId(), request.getProducerId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/subscribe")
    @Operation(summary = "Отписка от пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Удалено"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    public ResponseEntity<String> unsubscribe(@RequestBody SubscriptionRequest request) {
        try {
            mssService.unsubscribe(request.getSubscriberId(), request.getProducerId());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/subscribe/status")
    @Operation(summary = "Статус подписки", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "boolean")))
    })
    public ResponseEntity<Boolean> isSubscribed(@RequestParam Integer subscriberId, @RequestParam Integer producerId) {
        boolean status = mssService.isSubscribed(subscriberId, producerId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
