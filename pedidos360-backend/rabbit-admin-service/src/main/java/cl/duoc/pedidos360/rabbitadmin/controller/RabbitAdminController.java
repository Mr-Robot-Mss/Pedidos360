package cl.duoc.pedidos360.rabbitadmin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.pedidos360.rabbitadmin.dto.BindingRequest;
import cl.duoc.pedidos360.rabbitadmin.dto.ExchangeRequest;
import cl.duoc.pedidos360.rabbitadmin.dto.QueueRequest;
import cl.duoc.pedidos360.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/internal/rabbit")
public class RabbitAdminController {
    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping("/queues")
    @ResponseStatus(HttpStatus.CREATED)
    public String createQueue(@Valid @RequestBody QueueRequest request) {
        return service.createQueue(request);
    }

    @DeleteMapping("/queues/{name}")
    public String deleteQueue(@PathVariable @NotBlank String name) {
        return service.deleteQueue(name);
    }

    @PostMapping("/exchanges")
    @ResponseStatus(HttpStatus.CREATED)
    public String createExchange(@Valid @RequestBody ExchangeRequest request) {
        return service.createExchange(request);
    }

    @DeleteMapping("/exchanges/{name}")
    public String deleteExchange(@PathVariable @NotBlank String name) {
        return service.deleteExchange(name);
    }

    @PostMapping("/bindings")
    @ResponseStatus(HttpStatus.CREATED)
    public String createBinding(@Valid @RequestBody BindingRequest request) {
        return service.createBinding(request);
    }

    @DeleteMapping("/bindings")
    public String deleteBinding(@Valid @RequestBody BindingRequest request) {
        return service.deleteBinding(request);
    }
}
