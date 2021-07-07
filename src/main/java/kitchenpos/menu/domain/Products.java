package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kitchenpos.common.domian.Price;
import kitchenpos.common.error.InvalidRequestException;
import kitchenpos.product.domain.Product;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products, int requestSize) {
        checkRequestSize(products, requestSize);
        this.products = Collections.unmodifiableList(new ArrayList<>(products));
    }

    private void checkRequestSize(List<Product> products, int requestSize) {
        if (products.size() != requestSize) {
            throw new InvalidRequestException();
        }
    }

    public Price totalPrice(Quantities quantities) {
        Price totalPrice = new Price();
        for (Product product : products) {
            totalPrice = product.priceMultiplyQuantity(quantities.getByProductId(product.getId()));
        }
        return totalPrice;
    }

    public List<MenuProduct> toMenuProducts(Menu menu, Quantities quantities) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Product product : products) {
            menuProducts.add(MenuProduct.of(menu, product, quantities.getByProductId(product.getId())));
        }
        return menuProducts;
    }
}
