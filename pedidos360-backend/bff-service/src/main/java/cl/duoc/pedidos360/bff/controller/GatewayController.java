package cl.duoc.pedidos360.bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api")
public class GatewayController {
    private final RestClient client;
    private final String ordersUrl;
    private final String catalogUrl;

    public GatewayController(RestClient client,
            @Value("${services.orders-url}") String ordersUrl,
            @Value("${services.catalog-url}") String catalogUrl) {
        this.client = client;
        this.ordersUrl = ordersUrl;
        this.catalogUrl = catalogUrl;
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('SCOPE_Pedidos.ReadWrite') or hasAnyRole('Pedidos.Admin','Pedidos.Operador')")
    public ResponseEntity<String> orders() {
        String body = client.get().uri(ordersUrl + "/internal/orders").retrieve().body(String.class);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(body);
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('SCOPE_Pedidos.ReadWrite') or hasAnyRole('Pedidos.Admin','Pedidos.Operador')")
    public ResponseEntity<String> createOrder(@RequestBody String orderJson) {
        String body = client.post()
                .uri(ordersUrl + "/internal/orders")
                .header("Content-Type", "application/json")
                .body(orderJson)
                .retrieve()
                .body(String.class);
        return ResponseEntity.status(201).header("Content-Type", "application/json").body(body);
    }

    @GetMapping("/catalog")
    @PreAuthorize("hasAuthority('SCOPE_Pedidos.ReadWrite')")
    public ResponseEntity<String> catalog() {
        String body = client.get().uri(catalogUrl + "/internal/products").retrieve().body(String.class);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(body);
    }
}
