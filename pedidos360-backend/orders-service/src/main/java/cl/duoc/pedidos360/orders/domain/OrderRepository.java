package cl.duoc.pedidos360.orders.domain;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {}
