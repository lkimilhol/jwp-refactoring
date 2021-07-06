package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;

@DisplayName("주문 도메인 테스트")
class OrderTest {

    @DisplayName("생성")
    @Test
    void create() {
        // given
        // when
        Order order = Order.of(1L, OrderStatus.MEAL);
        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        // given
        Order order = Order.of(1L, OrderStatus.MEAL);
        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문 상태 체크 - COOKING, MEAL인지")
    @Test
    void checkChangeableStatus() {
        // given
        Order order = Order.of(1L, OrderStatus.MEAL);
        // when
        // then
        assertThatThrownBy(order::checkChangeableStatus)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.INVALID_ORDER_STATUS.message());
    }
}