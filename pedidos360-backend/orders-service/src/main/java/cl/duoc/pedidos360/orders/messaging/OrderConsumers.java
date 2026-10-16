package cl.duoc.pedidos360.orders.messaging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class OrderConsumers {
    private static final Logger log = LoggerFactory.getLogger(OrderConsumers.class);

    @RabbitListener(queues = "${pedidos360.rabbitmq.queues.notifications}")
    public void consumeNotification(OrderCreatedMessage payload, Message message, Channel channel) throws IOException {
        handle("notificacion", payload, message, channel);
    }

    @RabbitListener(queues = "${pedidos360.rabbitmq.queues.tickets}")
    public void consumeTicket(OrderCreatedMessage payload, Message message, Channel channel) throws IOException {
        handle("ticket", payload, message, channel);
    }

    @RabbitListener(queues = "${pedidos360.rabbitmq.queues.dlq}")
    public void consumeDeadLetter(Message message, Channel channel) throws IOException {
        log.error("Mensaje recibido en DLQ routingKey={} body={}",
                message.getMessageProperties().getReceivedRoutingKey(),
                new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    private void handle(String useCase, OrderCreatedMessage payload, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            validate(payload);
            log.info("Procesando {} asincrono para pedido {} cliente={}", useCase, payload.orderId(), payload.customerName());
            channel.basicAck(deliveryTag, false);
        } catch (IllegalArgumentException ex) {
            log.error("Mensaje no recuperable en {}. Se envia a DLQ. Motivo={}", useCase, ex.getMessage());
            channel.basicReject(deliveryTag, false);
        } catch (Exception ex) {
            boolean alreadyRetried = Boolean.TRUE.equals(message.getMessageProperties().getRedelivered());
            if (alreadyRetried) {
                log.error("Error repetido en {}. Se envia a DLQ. Motivo={}", useCase, ex.getMessage());
                channel.basicReject(deliveryTag, false);
                return;
            }
            log.warn("Error recuperable procesando {}. Se reintenta el mensaje. Motivo={}", useCase, ex.getMessage());
            channel.basicNack(deliveryTag, false, true);
        }
    }

    private void validate(OrderCreatedMessage payload) {
        if (payload.orderId() == null || payload.customerName() == null || payload.customerName().isBlank()) {
            throw new IllegalArgumentException("pedido incompleto");
        }
    }
}
