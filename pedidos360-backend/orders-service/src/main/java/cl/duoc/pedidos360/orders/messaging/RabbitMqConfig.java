package cl.duoc.pedidos360.orders.messaging;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitNames.class)
public class RabbitMqConfig {
    @Bean
    DirectExchange ordersExchange(RabbitNames names) {
        return new DirectExchange(names.getExchanges().getOrders(), true, false);
    }

    @Bean
    DirectExchange deadLetterExchange(RabbitNames names) {
        return new DirectExchange(names.getExchanges().getDlx(), true, false);
    }

    @Bean
    Queue notificationsQueue(RabbitNames names) {
        return QueueBuilder.durable(names.getQueues().getNotifications())
                .withArguments(dlqArguments(names))
                .build();
    }

    @Bean
    Queue ticketsQueue(RabbitNames names) {
        return QueueBuilder.durable(names.getQueues().getTickets())
                .withArguments(dlqArguments(names))
                .build();
    }

    @Bean
    Queue deadLetterQueue(RabbitNames names) {
        return QueueBuilder.durable(names.getQueues().getDlq()).build();
    }

    @Bean
    Binding notificationBinding(Queue notificationsQueue, DirectExchange ordersExchange, RabbitNames names) {
        return BindingBuilder.bind(notificationsQueue).to(ordersExchange).with(names.getRoutingKeys().getNotification());
    }

    @Bean
    Binding ticketBinding(Queue ticketsQueue, DirectExchange ordersExchange, RabbitNames names) {
        return BindingBuilder.bind(ticketsQueue).to(ordersExchange).with(names.getRoutingKeys().getTicket());
    }

    @Bean
    Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange, RabbitNames names) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(names.getRoutingKeys().getDead());
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAcknowledgeMode(org.springframework.amqp.core.AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(5);
        return factory;
    }

    private Map<String, Object> dlqArguments(RabbitNames names) {
        return Map.of(
                "x-dead-letter-exchange", names.getExchanges().getDlx(),
                "x-dead-letter-routing-key", names.getRoutingKeys().getDead(),
                "x-message-ttl", 30000);
    }
}
