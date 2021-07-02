package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setup() {
        order = new Order();
        order.setId(1L);
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItem)));

        // when
        when(orderDao.save(any())).thenReturn(order);
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable()));
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @DisplayName("주문 조회")
    @Test
    void findAll() {
        // given

        // when
        when(orderDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(order)));
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(new ArrayList<>(Arrays.asList(new OrderLineItem())));
        List<Order> orders = orderService.list();
        // then
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getId()).isEqualTo(1L);
        assertThat(order.getOrderLineItems().size()).isEqualTo(1L);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        // given
        Order targetOrder = new Order();
        targetOrder.setOrderStatus(MEAL.name());
        order.setOrderStatus(COOKING.name());

        // when
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));
        Order changedOrder = orderService.changeOrderStatus(1L, targetOrder);
        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("생성 실패 - 주문 항목이 비어있음")
    @Test
    void createFailedByOrderLineItems() {
        // given
        // when
        // then
        assertThatThrownBy(() -> orderService.create(new Order())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성 실패 - 주문 항목의 개수와 메뉴 개수가 일치하지 않음")
    @Test
    void createFailedByMenus() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem());
        orderLineItems.add(new OrderLineItem());
        order.setOrderLineItems(orderLineItems);

        // when
        when(menuDao.countByIdIn(any())).thenReturn(1L);

        // then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성 실패 - 주문테이블이 비어있음")
    @Test
    void createFailedByOrderTable() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItem)));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        // then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }
}