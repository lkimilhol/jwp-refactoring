package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponseDto;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 테스트")
class MenuGroupServiceTest {
    public static final String menuName = "국밥";

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setup() {
        menuGroup = new MenuGroup(menuName);
    }

    @DisplayName("사용자는 메뉴 그룹을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(menuName);
        // when
        when(menuGroupDao.save(any())).thenReturn(menuGroup);
        MenuGroupResponseDto createdMenuGroup = menuGroupService.create(menuGroupRequest);
        // then
        assertThat(createdMenuGroup).isNotNull();
        assertThat(createdMenuGroup.getName()).isEqualTo(menuName);
    }

    @DisplayName("사용자는 메뉴 그룹 전체를 조회 할 수 있다.")
    @Test
    void find() {
        // given

        // when
        when(menuGroupDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(menuGroup)));
        MenuGroupResponse menuGroups = menuGroupService.list();
        // then
        System.out.println(menuGroups.getMenuGroupResponses().size());
        assertThat(menuGroups.getMenuGroupResponses().size()).isEqualTo(1);
        assertThat(menuGroups.getMenuGroupResponses().get(0).getName()).isEqualTo(menuName);
    }
}