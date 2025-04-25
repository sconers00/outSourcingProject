package com.example.outsourcingproject.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.order.entity.Orders;
import com.example.outsourcingproject.order.enums.OrderStatus;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.user.entity.User;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	@Query("SELECT o FROM Orders o WHERE o.user = :user")
	Optional<List<Orders>> findAllByUser(@Param("user") User user);

	default PageImpl<Orders> findAllByUserOrElseThrow(User user, PageRequest pageRequest) {
		List<Orders> foundList = findAllByUser(user).orElseThrow(
			() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		int start = (int)pageRequest.getOffset();
		int end = Math.min(start + pageRequest.getPageSize(), foundList.size());
		return new PageImpl<>(foundList.subList(start, end), pageRequest, foundList.size());
	}

	Optional<List<Orders>> findAllByStore(Store store);

	default PageImpl<Orders> findAllByStoreOrElseThrow(Store store, PageRequest pageRequest) {
		List<Orders> foundList = findAllByStore(store).orElseThrow(
			() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		int start = (int)pageRequest.getOffset();
		int end = Math.min(start + pageRequest.getPageSize(), foundList.size());
		return new PageImpl<>(foundList.subList(start, end), pageRequest, foundList.size());
	}

	Optional<List<Orders>> findAllByOrderStatusAndStore(OrderStatus status, Store store);

	default PageImpl<Orders> findAllByOrderStatusAndStoreOrElseThrow(OrderStatus status, Store store,
		PageRequest pageRequest) {
		List<Orders> foundList = findAllByOrderStatusAndStore(status, store).orElseThrow(
			() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		int start = (int)pageRequest.getOffset();
		int end = Math.min(start + pageRequest.getPageSize(), foundList.size());
		return new PageImpl<>(foundList.subList(start, end), pageRequest, foundList.size());
	}
}
