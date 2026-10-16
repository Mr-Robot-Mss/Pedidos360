package cl.duoc.pedidos360.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ExchangeRequest(
        @NotBlank String name,
        @Pattern(regexp = "direct|fanout|topic", message = "type debe ser direct, fanout o topic") String type,
        boolean durable) {
}
