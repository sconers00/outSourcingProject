package com.example.outsourcingproject.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.service.MenuService;
import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;
import com.example.outsourcingproject.store.dto.responseDto.GetStoreResponseDto;
import com.example.outsourcingproject.store.dto.responseDto.StoreResponseDto;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.enums.UserRole;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private MenuService menuService;
	@Mock
	private HttpServletRequest httpServletRequest;

	@Mock
	private User mockUser;
	@Mock
	private Store mockStore;
	@Mock
	private StoreRequestDto storeRequestDto;

	@InjectMocks
	StoreService storeService;

	@Nested
	class registerStore {
		@Test
		void create() {
			//given
			User user = User.builder()
				.userRole(UserRole.OWNER)
				.build();
			Store store = Store.builder()
				.address("testAddr")
				.closeTime("12:34")
				.minOrderPrice(1234L)
				.openTime("12:34")
				.storeName("testStore")
				.storeTelNumber("123-4567-8900")
				.user(mockUser)
				.build();
			StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
				1234L);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(any())).willReturn(Optional.of(user));
			given(storeRepository.countStoresByUser(any())).willReturn(1L);
			given(storeRepository.save(any())).willReturn(store);

			//when
			StoreResponseDto responseDto = storeService.registerStore(requestDto, any());

			//then
			assertEquals("testAddr", responseDto.getAddress());
		}

		@Test
		void userNotOwner() {
			//given
			User user = User.builder()
				.userRole(UserRole.CUSTOMER)
				.build();
			StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
				1234L);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(any())).willReturn(Optional.of(user));
			given(storeRepository.countStoresByUser(any())).willReturn(1L);

			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> storeService.registerStore(requestDto, any()));

			//then
			assertEquals("403 FORBIDDEN \"사장님만이 가게를 등록할 수 있습니다.\"", exception.getMessage());
		}

		@Test
		void storeMoreThenThree() {
			//given
			User user = User.builder()
				.userRole(UserRole.OWNER)
				.build();
			StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
				1234L);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(any())).willReturn(Optional.of(user));
			given(storeRepository.countStoresByUser(any())).willReturn(4L);

			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> storeService.registerStore(requestDto, any()));

			//then
			assertEquals("400 BAD_REQUEST \"가게를 세 개 초과해서 등록할 수 없습니다.\"", exception.getMessage());
		}

		@Test
		void storeAlreadyExist() {
			//given
			User user = User.builder()
				.userRole(UserRole.OWNER)
				.build();
			StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
				1234L);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(any())).willReturn(Optional.of(user));
			given(storeRepository.countStoresByUser(any())).willReturn(1L);
			given(storeRepository.findByStoreName(anyString())).willReturn(Optional.of(mockStore));

			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> storeService.registerStore(requestDto, any()));

			//then
			assertEquals("409 CONFLICT \"이미 존재하는 가게 이름입니다.\"", exception.getMessage());
		}

		@Test
		void storeTelAlreadyExist() {
			//given
			User user = User.builder()
				.userRole(UserRole.OWNER)
				.build();
			StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
				1234L);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(any())).willReturn(Optional.of(user));
			given(storeRepository.countStoresByUser(any())).willReturn(1L);
			given(storeRepository.findByStoreTelNumber(anyString())).willReturn(Optional.of(mockStore));

			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> storeService.registerStore(requestDto, any()));

			//then
			assertEquals("409 CONFLICT \"이미 존재하는 가게 전화번호입니다.\"", exception.getMessage());
		}
	}

	@Nested
	class UpdateById {
		@Test
		void updateById() {
			//given
			StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
				1234L);
			Store store = Store.builder()
				.user(mockUser)
				.build();
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(storeRepository.findByIdOrElseThrow(anyLong())).willReturn(store);
			given(mockUser.getUserId()).willReturn(1L);

			//when
			StoreResponseDto responseDto = storeService.updateById(1L, requestDto, httpServletRequest);

			//then
			assertEquals(requestDto.getAddress(), responseDto.getAddress());
			assertEquals(requestDto.getStoreName(), responseDto.getStoreName());
			assertEquals(requestDto.getCloseTime(), responseDto.getCloseTime());
			assertEquals(requestDto.getOpenTime(), responseDto.getOpenTime());
			assertEquals(requestDto.getStoreTelNumber(), responseDto.getStoreTelNumber());
		}

		@Test
		void wrongUser() {
			//given
			StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
				1234L);
			Store store = Store.builder()
				.user(mockUser)
				.build();
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(storeRepository.findByIdOrElseThrow(anyLong())).willReturn(store);
			given(mockUser.getUserId()).willReturn(2L);

			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> storeService.updateById(1L, requestDto, httpServletRequest));

			//then
			assertEquals("403 FORBIDDEN \"수정 권한이 없습니다.\"", exception.getMessage());
		}
	}

	@Test
	void findById() {
		//given
		Store store = Store.builder()
			.storeName("testStore")
			.build();
		List<MenuResponseDto> list = new ArrayList<>();
		MenuResponseDto menuResponseDto = MenuResponseDto.builder()
			.menuName("testMenu")
			.build();
		list.add(menuResponseDto);
		given(storeRepository.findByIdOrElseThrow(anyLong())).willReturn(store);
		given(menuService.findByStoreId(any())).willReturn(list);
		//when
		GetStoreResponseDto responseDto = storeService.findById(1L);
		//then
		assertEquals("testStore", responseDto.getStoreName());
		assertEquals("testMenu", responseDto.getMenuList().get(0).getMenuName());
	}

	@Test
	void findAll() {
		//given
		Store store = Store.builder()
			.storeName("testStore")
			.build();
		List<Store> storeList = new ArrayList<>();
		storeList.add(store);
		given(storeRepository.findAll()).willReturn(storeList);

		//when
		List<StoreResponseDto> list = storeService.findAll();

		//then
		assertEquals("testStore", list.get(0).getStoreName());
	}

	@Nested
	class delete {
		@Test
		void delete() {
			//given
			Store store = Store.builder()
				.user(mockUser)
				.build();
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(storeRepository.findByIdOrElseThrow(anyLong())).willReturn(store);
			given(mockUser.getUserId()).willReturn(1L);

			//when
			storeService.delete(1L, httpServletRequest);
			ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

			//then
			verify(storeRepository).delete(captor.capture());
			assertEquals(store, captor.getValue());
		}

		@Test
		void wrongUser() {
			//given
			Store store = Store.builder()
				.user(mockUser)
				.build();
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(storeRepository.findByIdOrElseThrow(anyLong())).willReturn(store);
			given(mockUser.getUserId()).willReturn(2L);

			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> storeService.delete(1L, httpServletRequest));

			//then
			assertEquals("403 FORBIDDEN \"삭제 권한이 없습니다.\"", exception.getMessage());
		}
	}
}