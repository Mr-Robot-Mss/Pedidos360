package cl.duoc.pedidos360.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record QueueRequest(
        @NotBlank String name,
        boolean durable,
        String deadLetterExchange,
        String deadLetterRoutingKey,
        @PositiveOrZero Integer messageTtl) {
}
