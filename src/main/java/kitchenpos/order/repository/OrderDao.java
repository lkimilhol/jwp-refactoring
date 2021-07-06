package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.Order;

public interface OrderDao extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(Object any, Object any1);
    void existsByOrderTableIdAndOrderStatusIn(Object any, Object any1);
}