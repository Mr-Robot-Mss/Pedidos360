package cl.duoc.pedidos360.orders.domain;
import java.math.BigDecimal;
import java.time.Instant;
import jakarta.persistence.*;
@Entity @Table(name = "customer_orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String customerName;
    @Enumerated(EnumType.STRING) private OrderStatus status;
    private BigDecimal total;
    private Instant createdAt;
    protected CustomerOrder() {}
    public CustomerOrder(String customerName, OrderStatus status, BigDecimal total) { this.customerName = customerName; this.status = status; this.total = total; this.createdAt = Instant.now(); }
    @PrePersist void prePersist() { if (createdAt == null) createdAt = Instant.now(); }
    public Long getId() { return id; } public String getCustomerName() { return customerName; } public OrderStatus getStatus() { return status; } public BigDecimal getTotal() { return total; } public Instant getCreatedAt() { return createdAt; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
