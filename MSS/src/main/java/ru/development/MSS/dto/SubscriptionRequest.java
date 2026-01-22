package ru.development.MSS.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "SubscriptionRequest", description = "Запрос на подписку")
public class SubscriptionRequest {
    @Schema(description = "Идентификатор подписчика", example = "20")
    private Integer subscriberId;

    @Schema(description = "Идентификатор продюсера", example = "10")
    private Integer producerId;
}
