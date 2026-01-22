package ru.development.MSS.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "Message", description = "Сообщение")
public class Message {
    @Schema(description = "Идентификатор сообщения", example = "1")
    private Integer id;

    @Schema(description = "Содержимое сообщения")
    private String content;

    @Schema(description = "Время создания (epoch seconds)")
    private Integer created;

    @Schema(description = "Идентификатор продюсера", example = "10")
    private Integer producerId;
}
