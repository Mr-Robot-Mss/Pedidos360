package cl.duoc.pedidos360.catalog.domain;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private BigDecimal price;
    protected Product() {}
    public Product(String name, BigDecimal price) { this.name = name; this.price = price; }
    public Long getId() { return id; } public String getName() { return name; } public BigDecimal getPrice() { return price; }
}
