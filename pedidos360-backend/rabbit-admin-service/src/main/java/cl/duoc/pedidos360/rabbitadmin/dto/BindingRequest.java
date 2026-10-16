package cl.duoc.pedidos360.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BindingRequest(
        @NotBlank String queue,
        @NotBlank String exchange,
        @Pattern(regexp = "direct|fanout|topic", message = "exchangeType debe ser direct, fanout o topic") String exchangeType,
        @NotBlank String routingKey) {
}
