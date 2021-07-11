package kitchenpos.order.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.error.InvalidOrderStatusException;
import kitchenpos.error.OrderStatusException;

@Entity
@Table(name = "orders")
public class Order {

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    private Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void checkChangeableStatus() {
        if (orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL)) {
            throw new InvalidOrderStatusException();
        }
    }

    public void checkAlreadyComplete() {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new OrderStatusException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
