package com.example.outsourcingproject.user.service;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.order.entity.Orders;
import com.example.outsourcingproject.order.enums.OrderStatus;
import com.example.outsourcingproject.order.service.OrderService;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.user.dto.SearchOrderResponse;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private Store mockStore;
	@Mock
	private Menu mockMenu;
	@Mock
	private User mockUser;
	@Mock
	private UserRepository userRepository;
	@Mock
	private OrderService orderService;
	@Mock
	HttpServletRequest httpServletRequest;
	@Mock
	private JwtUtil jwtUtil;
	@InjectMocks
	private UserService userService;

	@Test
	void deleteAccount() {
		//given
		given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
		given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));
		//when
		userService.deleteAccount(httpServletRequest);
		//then
		verify(mockUser).deleteAccount(true);
	}

	@Nested
	class SearchRequestedOrder {
		@Test
		void searchSuccess() {
			//given
			List<SearchOrderResponse> orderList = new ArrayList<>();
			Orders orders = Orders.builder()
				.user(mockUser)
				.address("testAddr")
				.menu(mockMenu)
				.orderStatus(OrderStatus.PENDING)
				.quantity(1L)
				.store(mockStore)
				.build();
			List<Orders> ol = new ArrayList<>();
			ol.add(orders);
			SearchOrderResponse so = new SearchOrderResponse(orders);
			orderList.add(so);
			given(jwtUtil.getIdFromRequest(httpServletRequest)).willReturn(1L);
			given(userRepository.findById(any())).willReturn(Optional.of(new User()));
			given(orderService.findOrderByUser(any(), any())).willReturn(ol);

			//when
			List<SearchOrderResponse> list = userService.searchRequestedOrder(httpServletRequest, 1);

			//then
			assertEquals(orderList.get(0).getAddress(), list.get(0).getAddress());
			assertEquals(orderList.get(0).getQuantity(), list.get(0).getQuantity());
			assertEquals(orderList.get(0).getOrderStatus(), list.get(0).getOrderStatus());
		}

		@Test
		void noOrder() {
			//given
			List<Orders> ol = new ArrayList<>();

			given(jwtUtil.getIdFromRequest(httpServletRequest)).willReturn(1L);
			given(userRepository.findById(any())).willReturn(Optional.of(new User()));
			given(orderService.findOrderByUser(any(), any())).willReturn(ol);

			//when
			ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
				() -> userService.searchRequestedOrder(httpServletRequest, 1));

			//then
			assertEquals("404 NOT_FOUND \"주문이 존재하지 않습니다.\"", responseStatusException.getMessage());
		}
	}

}