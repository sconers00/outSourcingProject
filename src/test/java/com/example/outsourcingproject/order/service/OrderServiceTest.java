package com.example.outsourcingproject.order.service;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.menu.repository.MenuRepository;
import com.example.outsourcingproject.order.dto.OrderRequestDto;
import com.example.outsourcingproject.order.dto.OrderResponse;
import com.example.outsourcingproject.order.entity.Orders;
import com.example.outsourcingproject.order.enums.OrderStatus;
import com.example.outsourcingproject.order.repository.OrderRepository;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.example.outsourcingproject.user.dto.SearchOrderResponse;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.enums.UserRole;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderRequestDto orderRequestDto;
	@Mock
	private HttpServletRequest httpServletRequest;
	@Mock
	private Store mockStore;
	@Mock
	private Menu mockMenu;
	@Mock
	private User mockUser;
	@Mock
	private PageRequest pageRequest;

	@InjectMocks
	OrderService orderService;

	@Nested
	class CancelOrder {
		@Test
		void cancelOrder() {
			User user = User.builder()
				.userRole(UserRole.CUSTOMER)
				.build();
			Orders orders = Orders.builder()
				.store(mockStore)
				.quantity(1L)
				.orderStatus(OrderStatus.PENDING)
				.menu(mockMenu)
				.address("testAddr")
				.user(user)
				.build();

			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
			given(orderRepository.findById(anyLong())).willReturn(Optional.of(orders));
			OrderResponse response = orderService.cancelOrder(any(), 1L);
			assertEquals("testAddr", response.getAddress());
		}

		@Test
		void userIsNotCustomer() {
			User user = User.builder()
				.userRole(UserRole.OWNER)
				.build();

			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.cancelOrder(any(), 1L));

			assertEquals("401 UNAUTHORIZED \"손님이 아니면 접근할 수 없습니다.\"", exception.getMessage());
		}
	}

	@Nested
	class CreateOrder {
		@Test
		void orderCreate() {
			User user = User.builder()
				.userRole(UserRole.CUSTOMER)
				.build();
			Orders orders = Orders.builder()
				.store(mockStore)
				.quantity(1L)
				.orderStatus(OrderStatus.PENDING)
				.menu(mockMenu)
				.address("testAddr")
				.user(mockUser)
				.build();
			OrderResponse orderResponse = new OrderResponse(orders);

			given(storeRepository.findById(any())).willReturn(Optional.of(mockStore));
			given(menuRepository.findById(any())).willReturn(Optional.of(mockMenu));
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
			given(orderRepository.save(any())).willReturn(orders);
			OrderResponse response = orderService.createOrder(orderRequestDto, httpServletRequest);

			assertEquals(response.getAddress(), orderResponse.getAddress());
		}

		@Test
		void userIsNotCustomer() {
			User user = User.builder()
				.userRole(UserRole.OWNER)
				.build();
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
			ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
				() -> orderService.createOrder(orderRequestDto, httpServletRequest));
			assertEquals("401 UNAUTHORIZED \"손님이 아닌 유저는 주문할 수 없습니다.\"", responseStatusException.getMessage());
		}
	}

	@Nested
	class changeOrderState {
		@Test
		void changeStateAsOwner() {
			// given
			Orders orders = Orders.builder()
				.user(mockUser)
				.address("testAddr")
				.menu(mockMenu)
				.orderStatus(OrderStatus.PENDING)
				.quantity(1L)
				.store(mockStore)
				.build();

			given(mockUser.getUserRole()).willReturn(UserRole.OWNER);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
			given(orderRepository.findById(anyLong())).willReturn(Optional.of(orders));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			//when
			OrderResponse response = orderService.changeOrderState(httpServletRequest, "ARRIVED", 1L, 1L);
			//then
			assertEquals("testAddr", response.getAddress());
			assertEquals(OrderStatus.ARRIVED.toString(), response.getOrderStatus());
		}

		@Test
		void customer() {
			// given
			Orders orders = Orders.builder()
				.user(mockUser)
				.address("testAddr")
				.menu(mockMenu)
				.orderStatus(OrderStatus.PENDING)
				.quantity(1L)
				.store(mockStore)
				.build();

			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
			given(mockUser.getUserRole()).willReturn(UserRole.CUSTOMER);
			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.changeOrderState(httpServletRequest, "ARRIVED", 1L, 1L));
			//then
			assertEquals("401 UNAUTHORIZED \"손님은 접근할 수 없습니다.\"", exception.getMessage());
		}

		@Test
		void wrongStore() {
			// given
			Orders orders = Orders.builder()
				.user(mockUser)
				.address("testAddr")
				.menu(mockMenu)
				.orderStatus(OrderStatus.PENDING)
				.quantity(1L)
				.store(new Store())
				.build();
			Store store = Store.builder()
				.user(mockUser)
				.build();

			given(mockUser.getUserRole()).willReturn(UserRole.OWNER);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
			given(orderRepository.findById(anyLong())).willReturn(Optional.of(orders));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.changeOrderState(httpServletRequest, "ARRIVED", 1L, 1L));
			//then
			assertEquals("401 UNAUTHORIZED \"해당 가게의 주문이 아닙니다.\"", exception.getMessage());
		}

		@Test
		void wrongOwner() {
			// given
			Orders orders = Orders.builder()
				.user(mockUser)
				.address("testAddr")
				.menu(mockMenu)
				.orderStatus(OrderStatus.PENDING)
				.quantity(1L)
				.store(mockStore)
				.build();

			Store store = Store.builder()
				.user(new User())
				.build();

			given(mockUser.getUserRole()).willReturn(UserRole.OWNER);
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
			given(orderRepository.findById(anyLong())).willReturn(Optional.of(orders));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.changeOrderState(httpServletRequest, "ARRIVED", 1L, 1L));
			//then
			assertEquals("401 UNAUTHORIZED \"해당 가게의 주인이 아닙니다.\"", exception.getMessage());
		}
	}

	@Nested
	class FindOrderByStore {

		@Test
		void findALLOrderByStore() {
			//given
			List<Orders> listToPage = new ArrayList<>();
			Orders orders = Orders.builder()
				.store(mockStore)
				.quantity(1L)
				.menu(mockMenu)
				.address("testAddr")
				.user(mockUser)
				.orderStatus(OrderStatus.PENDING)
				.build();
			listToPage.add(orders);
			PageImpl<Orders> page = new PageImpl<>(listToPage);

			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(orderRepository.findAllByStoreOrElseThrow(any(), any())).willReturn(page);

			//when
			List<SearchOrderResponse> list = orderService.findOrderByStore(httpServletRequest, 1L, "ALL", 1);

			//then
			assertEquals("testAddr", list.get(0).getAddress());
			assertEquals(OrderStatus.PENDING.toString(), list.get(0).getOrderStatus());
			assertEquals(1L, list.get(0).getQuantity());
		}

		@Test
		void findCertainOrderByStore() {
			//given
			List<Orders> listToPage = new ArrayList<>();
			Orders orders = Orders.builder()
				.store(mockStore)
				.quantity(1L)
				.menu(mockMenu)
				.address("testAddr")
				.user(mockUser)
				.orderStatus(OrderStatus.PENDING)
				.build();
			listToPage.add(orders);
			PageImpl<Orders> page = new PageImpl<>(listToPage);

			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(mockUser);
			given(orderRepository.findAllByOrderStatusAndStoreOrElseThrow(any(), any(), any())).willReturn(page);

			//when
			List<SearchOrderResponse> list = orderService.findOrderByStore(httpServletRequest, 1L, "PENDING", 1);

			//then
			assertEquals("testAddr", list.get(0).getAddress());
			assertEquals(OrderStatus.PENDING.toString(), list.get(0).getOrderStatus());
			assertEquals(1L, list.get(0).getQuantity());
		}

		@Test
		void userIsNotOwner() {
			//given
			given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
			given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
			given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
			given(mockStore.getUser()).willReturn(new User());

			//when
			ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.findOrderByStore(httpServletRequest, 1L, "PENDING", 1));

			//then
			assertEquals("401 UNAUTHORIZED \"해당 가게의 주인이 아닙니다.\"", exception.getMessage());
		}
	}

	@Test
	void findOrderByUser() {
		//given
		List<Orders> listToPage = new ArrayList<>();
		Orders orders = Orders.builder()
			.store(mockStore)
			.quantity(1L)
			.menu(mockMenu)
			.address("testAddr")
			.user(mockUser)
			.orderStatus(OrderStatus.PENDING)
			.build();
		listToPage.add(orders);
		PageImpl<Orders> page = new PageImpl<>(listToPage);

		given(orderRepository.findAllByUserOrElseThrow(any(), any())).willReturn(page);

		//when
		List<Orders> list = orderService.findOrderByUser(mockUser, pageRequest);

		//then
		assertEquals("testAddr", list.get(0).getAddress());
		assertEquals(OrderStatus.PENDING, list.get(0).getOrderStatus());
		assertEquals(1L, list.get(0).getQuantity());
	}
}