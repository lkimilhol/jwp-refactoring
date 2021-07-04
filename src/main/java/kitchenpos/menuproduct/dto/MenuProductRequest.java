package kitchenpos.menuproduct.dto;

import kitchenpos.common.domian.Quantity;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {}

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return new Quantity(quantity);
    }
}