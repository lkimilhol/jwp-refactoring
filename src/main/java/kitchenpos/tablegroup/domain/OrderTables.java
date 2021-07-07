package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.common.error.OrderTableNotEmptyException;
import kitchenpos.common.error.InvalidRequestException;
import org.springframework.util.CollectionUtils;

import kitchenpos.ordertable.domain.OrderTable;

@Embeddable
public class OrderTables {
    public OrderTables() {}

    public static final int MIN_SIZE = 2;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        checkValid(orderTables);
        checkOrderTableIsEmpty(orderTables);
        return new OrderTables(orderTables);
    }

    private static void checkValid(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_SIZE) {
            throw new InvalidRequestException();
        }
    }

    private static void checkOrderTableIsEmpty(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {
            if (!orderTable.isEmpty()) {
                throw new OrderTableNotEmptyException();
            }
        });
    }

    public void init(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroup);
            orderTable.empty(false);
        }
    }

    public int size() {
        return orderTables.size();
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void unGroup() {
        orderTables.forEach(orderTable -> orderTable.setTableGroup(null));
    }
}
