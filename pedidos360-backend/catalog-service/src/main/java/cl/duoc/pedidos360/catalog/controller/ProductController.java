package cl.duoc.pedidos360.catalog.controller;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.duoc.pedidos360.catalog.domain.Product;
import cl.duoc.pedidos360.catalog.domain.ProductRepository;
@RestController @RequestMapping("/internal/products")
public class ProductController {
    private final ProductRepository repository;
    public ProductController(ProductRepository repository) { this.repository = repository; }
    @GetMapping public List<Product> list() { return repository.findAll(); }
    @Bean CommandLineRunner seedProducts(ProductRepository repo) { return args -> { if (repo.count() == 0) repo.saveAll(List.of(new Product("Notebook Pro", new BigDecimal("899990")), new Product("Monitor 27", new BigDecimal("219990")))); }; }
}
