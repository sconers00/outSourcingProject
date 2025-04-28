package com.example.outsourcingproject.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.dto.MenuRequestDto;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.exception.MismatchException;
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

	@Test
	void updateMenu() {
	}

	@Test
	void delete() {
	}

	@Test
	void userChecker() {
	}

	@Test
	void getStoreId() {
	}

	@Test
	void findByStoreId() {
	}
}