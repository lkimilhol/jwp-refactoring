package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 테스트")
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    private Menu menu;

    private MenuService menuService;

    @BeforeEach

    void setup() {
        menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setName("순대국");
        menu.setPrice(BigDecimal.valueOf(8000));
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(new MenuProduct())));

        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(8);

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));

        menu.setMenuProducts(new ArrayList<>(Collections.singletonList(menuProduct)));
        // when
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product));
        when(menuDao.save(menu)).thenReturn(menu);
        Menu createdMenu = menuService.create(this.menu);
        // then
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getId()).isEqualTo(1L);
    }

    @DisplayName("조회")
    @Test
    void findAll() {
        // given

        // when
        when(menuDao.findAll()).thenReturn(new ArrayList<>(Collections.singletonList(menu)));
        List<Menu> menus = menuService.list();
        // then
        assertThat(menus.size()).isEqualTo(1);
        assertThat(menus.get(0).getId()).isEqualTo(1L);
    }

    @DisplayName("생성 실패 - 가격이 0")
    @Test
    void createFailedByPriceZero() {
        // given
        menu.setPrice(BigDecimal.ZERO);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성 실패 - 메뉴그룹이 존재하지 않음")
    @Test
    void createFailedByMenuGroup() {
        // given
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성 실패 - 메뉴 가격과 일치하지 않음")
    @Test
    void createFailedByPrice() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(8);

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        menu.setMenuProducts(new ArrayList<>(Collections.singletonList(menuProduct)));
        // when
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product));
        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }
}