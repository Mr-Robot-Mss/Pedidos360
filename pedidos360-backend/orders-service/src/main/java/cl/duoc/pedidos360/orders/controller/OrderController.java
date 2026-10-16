package cl.duoc.pedidos360.orders.controller;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import cl.duoc.pedidos360.orders.domain.*;
import cl.duoc.pedidos360.orders.messaging.OrderEventPublisher;
@RestController @RequestMapping("/internal/orders")
public class OrderController {
    private final OrderRepository repository;
    private final OrderEventPublisher eventPublisher;
    public OrderController(OrderRepository repository, OrderEventPublisher eventPublisher) { this.repository = repository; this.eventPublisher = eventPublisher; }
    @GetMapping public List<CustomerOrder> list() { return repository.findAll(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrder create(@RequestBody CustomerOrder order) {
        CustomerOrder saved = repository.save(order);
        eventPublisher.publishOrderCreated(saved);
        return saved;
    }
    @Bean CommandLineRunner seedOrders(OrderRepository repo) { return args -> { if (repo.count() == 0) repo.saveAll(List.of(new CustomerOrder("Comercial Norte", OrderStatus.PENDIENTE, new BigDecimal("189990")), new CustomerOrder("Servicios Andinos", OrderStatus.EN_PREPARACION, new BigDecimal("459980")), new CustomerOrder("Tienda Central", OrderStatus.DESPACHADO, new BigDecimal("79990")))); }; }
}
