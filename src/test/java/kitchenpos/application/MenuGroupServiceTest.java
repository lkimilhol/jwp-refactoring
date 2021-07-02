package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 테스트")
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setup() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("국밥");

        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given

        // when
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);
        MenuGroup cratedMenuGroup = menuGroupService.create(this.menuGroup);
        // then
        assertThat(cratedMenuGroup).isNotNull();
        assertThat(cratedMenuGroup.getId()).isEqualTo(1L);
    }

    @DisplayName("조회")
    @Test
    void find() {
        // given

        // when
        when(menuGroupDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(menuGroup)));
        List<MenuGroup> menuGroups = menuGroupService.list();
        // then
        assertThat(menuGroups.size()).isEqualTo(1);
        assertThat(menuGroups.get(0).getId()).isEqualTo(1L);
    }
}
