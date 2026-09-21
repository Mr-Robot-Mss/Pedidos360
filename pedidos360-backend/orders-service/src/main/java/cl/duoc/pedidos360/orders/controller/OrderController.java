package cl.duoc.pedidos360.orders.controller;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import cl.duoc.pedidos360.orders.domain.*;
@RestController @RequestMapping("/internal/orders")
public class OrderController {
    private final OrderRepository repository;
    public OrderController(OrderRepository repository) { this.repository = repository; }
    @GetMapping public List<CustomerOrder> list() { return repository.findAll(); }
    @Bean CommandLineRunner seedOrders(OrderRepository repo) { return args -> { if (repo.count() == 0) repo.saveAll(List.of(new CustomerOrder("Comercial Norte", OrderStatus.PENDIENTE, new BigDecimal("189990")), new CustomerOrder("Servicios Andinos", OrderStatus.EN_PREPARACION, new BigDecimal("459980")), new CustomerOrder("Tienda Central", OrderStatus.DESPACHADO, new BigDecimal("79990")))); }; }
}
