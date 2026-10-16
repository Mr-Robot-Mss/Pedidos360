package cl.duoc.pedidos360.rabbitadmin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import cl.duoc.pedidos360.rabbitadmin.dto.BindingRequest;
import cl.duoc.pedidos360.rabbitadmin.dto.ExchangeRequest;
import cl.duoc.pedidos360.rabbitadmin.dto.QueueRequest;

@Service
public class RabbitAdminService {
    private final RabbitAdmin rabbitAdmin;

    public RabbitAdminService(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    public String createQueue(QueueRequest request) {
        Map<String, Object> arguments = new HashMap<>();
        if (hasText(request.deadLetterExchange())) {
            arguments.put("x-dead-letter-exchange", request.deadLetterExchange());
        }
        if (hasText(request.deadLetterRoutingKey())) {
            arguments.put("x-dead-letter-routing-key", request.deadLetterRoutingKey());
        }
        if (request.messageTtl() != null) {
            arguments.put("x-message-ttl", request.messageTtl());
        }

        Queue queue = request.durable()
                ? QueueBuilder.durable(request.name()).withArguments(arguments).build()
                : QueueBuilder.nonDurable(request.name()).withArguments(arguments).build();
        rabbitAdmin.declareQueue(queue);
        return "Cola creada: " + request.name();
    }

    public String deleteQueue(String name) {
        rabbitAdmin.deleteQueue(name);
        return "Cola eliminada: " + name;
    }

    public String createExchange(ExchangeRequest request) {
        String type = request.type() == null || request.type().isBlank() ? "direct" : request.type();
        switch (type) {
            case "fanout" -> rabbitAdmin.declareExchange(new FanoutExchange(request.name(), request.durable(), false));
            case "topic" -> rabbitAdmin.declareExchange(new TopicExchange(request.name(), request.durable(), false));
            default -> rabbitAdmin.declareExchange(new DirectExchange(request.name(), request.durable(), false));
        }
        return "Exchange creado: " + request.name();
    }

    public String deleteExchange(String name) {
        rabbitAdmin.deleteExchange(name);
        return "Exchange eliminado: " + name;
    }

    public String createBinding(BindingRequest request) {
        Binding binding = buildBinding(request);
        rabbitAdmin.declareBinding(binding);
        return "Binding creado: " + request.exchange() + " -> " + request.queue();
    }

    public String deleteBinding(BindingRequest request) {
        Binding binding = buildBinding(request);
        rabbitAdmin.removeBinding(binding);
        return "Binding eliminado: " + request.exchange() + " -> " + request.queue();
    }

    private Binding buildBinding(BindingRequest request) {
        String type = request.exchangeType() == null || request.exchangeType().isBlank() ? "direct" : request.exchangeType();
        Queue queue = new Queue(request.queue());
        return switch (type) {
            case "fanout" -> BindingBuilder.bind(queue).to(new FanoutExchange(request.exchange()));
            case "topic" -> BindingBuilder.bind(queue).to(new TopicExchange(request.exchange())).with(request.routingKey());
            default -> BindingBuilder.bind(queue).to(new DirectExchange(request.exchange())).with(request.routingKey());
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
