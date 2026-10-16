package cl.duoc.pedidos360.orders.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pedidos360.rabbitmq")
public class RabbitNames {
    private final Exchanges exchanges = new Exchanges();
    private final Queues queues = new Queues();
    private final RoutingKeys routingKeys = new RoutingKeys();

    public Exchanges getExchanges() { return exchanges; }
    public Queues getQueues() { return queues; }
    public RoutingKeys getRoutingKeys() { return routingKeys; }

    public static class Exchanges {
        private String orders;
        private String dlx;
        public String getOrders() { return orders; }
        public void setOrders(String orders) { this.orders = orders; }
        public String getDlx() { return dlx; }
        public void setDlx(String dlx) { this.dlx = dlx; }
    }

    public static class Queues {
        private String notifications;
        private String tickets;
        private String dlq;
        public String getNotifications() { return notifications; }
        public void setNotifications(String notifications) { this.notifications = notifications; }
        public String getTickets() { return tickets; }
        public void setTickets(String tickets) { this.tickets = tickets; }
        public String getDlq() { return dlq; }
        public void setDlq(String dlq) { this.dlq = dlq; }
    }

    public static class RoutingKeys {
        private String orderCreated;
        private String notification;
        private String ticket;
        private String dead;
        public String getOrderCreated() { return orderCreated; }
        public void setOrderCreated(String orderCreated) { this.orderCreated = orderCreated; }
        public String getNotification() { return notification; }
        public void setNotification(String notification) { this.notification = notification; }
        public String getTicket() { return ticket; }
        public void setTicket(String ticket) { this.ticket = ticket; }
        public String getDead() { return dead; }
        public void setDead(String dead) { this.dead = dead; }
    }
}
