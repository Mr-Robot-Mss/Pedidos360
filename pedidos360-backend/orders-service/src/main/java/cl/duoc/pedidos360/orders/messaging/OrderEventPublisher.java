package cl.duoc.pedidos360.orders.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import cl.duoc.pedidos360.orders.domain.CustomerOrder;

@Component
public class OrderEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitNames names;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate, RabbitNames names) {
        this.rabbitTemplate = rabbitTemplate;
        this.names = names;
    }

    public void publishOrderCreated(CustomerOrder order) {
        OrderCreatedMessage message = new OrderCreatedMessage(
                order.getId(),
                order.getCustomerName(),
                order.getStatus().name(),
                order.getTotal(),
                order.getCreatedAt());

        rabbitTemplate.convertAndSend(names.getExchanges().getOrders(), names.getRoutingKeys().getNotification(), message);
        rabbitTemplate.convertAndSend(names.getExchanges().getOrders(), names.getRoutingKeys().getTicket(), message);
        log.info("Evento de pedido publicado en RabbitMQ orderId={}", order.getId());
    }
}
