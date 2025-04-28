package com.example.outsourcingproject.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.dto.MenuDeleteResponseDto;
import com.example.outsourcingproject.menu.dto.MenuRequestDto;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.menu.exception.MismatchException;
import com.example.outsourcingproject.menu.exception.NotFoundException;
import com.example.outsourcingproject.menu.repository.MenuRepository;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.example.outsourcingproject.user.entity.User;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private HttpServletRequest httpServletRequest;

	@Mock
	private User mockUser;
	@Mock
	private Store mockStore;
	@Mock
	private Menu mockMenu;

	@InjectMocks
	private MenuService menuService;

	@Nested
	class Save {
		@Test
		void save() {
			//given
			MenuRequestDto requestDto = new MenuRequestDto("testMenu", 1234L, "test");
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);

			//when
			MenuResponseDto responseDto = menuService.save(requestDto, 1L, httpServletRequest);

			//then
			assertEquals(requestDto.getMenuName(), responseDto.getMenuName());
		}

		@Test
		void wrongUser() {
			//given
			MenuRequestDto requestDto = new MenuRequestDto("testMenu", 1234L, "test");
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);

			//when
			MismatchException exception = assertThrows(MismatchException.class,
				() -> menuService.save(requestDto, 1L, httpServletRequest));

			//then
			assertEquals("403 FORBIDDEN \"본인 소유의 점포에만 매뉴를 추가할 수 있습니다.\"", exception.getMessage());
		}
	}

	@Nested
	class UpdateMenu {
		@Test
		void updateMenu() {
			//given
			Menu menu = Menu.builder()
				.menuId(1L)
				.storeId(mockStore)
				.menuPrice(1234L)
				.menuName("testMenu")
				.discription("test")
				.build();
			MenuRequestDto request = new MenuRequestDto("changeMenu", 2345L, "update");
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

			//when
			MenuResponseDto responseDto = menuService.updateMenu(request, 1L, 1L, httpServletRequest);

			//then
			assertEquals("changeMenu", responseDto.getMenuName());
		}

		@Test
		void wrongOwner() {
			//given
			Store otherStore = Store.builder()
				.user(mockUser)
				.build();
			MenuRequestDto request = new MenuRequestDto("changeMenu", 2345L, "update");
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(otherStore));
			given(jwtUtil.getIdFromRequest(any())).willReturn(2L);
			given(mockUser.getUserId()).willReturn(1L);

			//when
			MismatchException exception = assertThrows(MismatchException.class,
				() -> menuService.updateMenu(request, 1L, 1L, httpServletRequest));

			//then
			assertEquals("403 FORBIDDEN \"본인 소유 점포의 매뉴만 수정할 수 있습니다.\"", exception.getMessage());
		}

		@Test
		void wrongMenu() {
			//given
			Menu menu = Menu.builder()
				.menuId(1L)
				.storeId(mockStore)
				.menuPrice(1234L)
				.menuName("testMenu")
				.discription("test")
				.build();
			MenuRequestDto request = new MenuRequestDto("changeMenu", 2345L, "update");
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

			//when
			NotFoundException exception = assertThrows(NotFoundException.class,
				() -> menuService.updateMenu(request, 1L, 1L, httpServletRequest));

			//then
			assertEquals("404 NOT_FOUND \"메뉴 ID가 잘못되었거나 없는 메뉴입니다.\"", exception.getMessage());
		}

		@Test
		void wrongStore() {
			//given
			Store otherStore = new Store();
			Menu menu = Menu.builder()
				.menuId(1L)
				.storeId(otherStore)
				.menuPrice(1234L)
				.menuName("testMenu")
				.discription("test")
				.build();
			MenuRequestDto request = new MenuRequestDto("changeMenu", 2345L, "update");
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
			given(mockStore.getId()).willReturn(1L);

			//when
			NotFoundException exception = assertThrows(NotFoundException.class,
				() -> menuService.updateMenu(request, 1L, 1L, httpServletRequest));

			//then
			assertEquals("400 BAD_REQUEST \"가게 ID와 메뉴 소유 점포 ID가 일치하지 않습니다.\"", exception.getMessage());
		}
	}

	@Nested
	class Delete {
		@Test
		void delete() {
			Menu menu = Menu.builder()
				.menuId(1L)
				.storeId(mockStore)
				.menuPrice(1234L)
				.menuName("testMenu")
				.discription("test")
				.build();
			given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(mockStore.getId()).willReturn(1L);

			ResponseEntity<MenuDeleteResponseDto> responseEntity = menuService.delete(1L, 1L, httpServletRequest);

			assertEquals("메뉴가 삭제되었습니다.", responseEntity.getBody().getMessage());
		}

		@Test
		void wrongOwner() {
			//given
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(jwtUtil.getIdFromRequest(any())).willReturn(2L);

			//when
			MismatchException exception = assertThrows(MismatchException.class,
				() -> menuService.delete(1L, 1L, httpServletRequest));

			//then
			assertEquals("403 FORBIDDEN \"본인 소유 점포의 매뉴만 삭제할 수 있습니다.\"", exception.getMessage());
		}

		@Test
		void wrongMenu() {
			//given
			given(menuRepository.findById(anyLong())).willReturn(Optional.empty());
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);

			//when
			NotFoundException exception = assertThrows(NotFoundException.class,
				() -> menuService.delete(1L, 1L, httpServletRequest));

			//then
			assertEquals("404 NOT_FOUND \"메뉴 ID가 잘못되었거나 없는 메뉴입니다.\"", exception.getMessage());
		}

		@Test
		void wrongStore() {
			//given
			Menu menu = Menu.builder()
				.menuId(1L)
				.storeId(new Store())
				.menuPrice(1234L)
				.menuName("testMenu")
				.discription("test")
				.build();
			given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(mockStore.getId()).willReturn(1L);

			//when
			NotFoundException exception = assertThrows(NotFoundException.class,
				() -> menuService.delete(1L, 1L, httpServletRequest));

			//then
			assertEquals("400 BAD_REQUEST \"가게 ID와 메뉴 소유 점포 ID가 일치하지 않습니다.\"", exception.getMessage());
		}
	}

	@Test
	void userChecker() {
		//given
		given(mockStore.getUser()).willReturn(mockUser);
		//when
		Boolean result = menuService.userChecker(httpServletRequest, mockStore);
		//then
		assertEquals(false, result);
	}

	@Nested
	class GetStoreId {
		@Test
		void getStoreId() {
			//given
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			//when
			Store store = menuService.getStoreId(1L);
			//then
			assertEquals(store, mockStore);
		}

		@Test
		void noStore() {
			//given
			given(storeRepository.findById(anyLong())).willReturn(Optional.empty());
			//when
			NotFoundException exception = assertThrows(NotFoundException.class, () -> menuService.getStoreId(1L));
			//then
			assertEquals("404 NOT_FOUND \"가게 ID가 잘못되었거나 없는 가게입니다.\"", exception.getMessage());
		}
	}

	@Test
	void findByStoreId() {
		Menu menu = Menu.builder()
			.storeId(mockStore)
			.menuId(1L)
			.discription("test")
			.menuName("testMenu")
			.menuPrice(1234L)
			.isDeleted(false)
			.build();
		List<Menu> menuList = new ArrayList<>();
		menuList.add(menu);
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
		given(menuRepository.findByStoreId(any(), anyLong())).willReturn(menuList);
		List<MenuResponseDto> list = menuService.findByStoreId(1L);
		assertEquals("testMenu", list.get(0).getMenuName());
	}
}