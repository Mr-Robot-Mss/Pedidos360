package cl.duoc.pedidos360.orders.messaging;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedMessage(
        Long orderId,
        String customerName,
        String status,
        BigDecimal total,
        Instant createdAt) {
}
