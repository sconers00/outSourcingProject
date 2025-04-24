package com.example.outsourcingproject.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.order.entity.Order;
import com.example.outsourcingproject.user.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<List<Order>> findAllByUser(User user);

	default PageImpl<Order> findAllByUserOrElseThrow(User user, PageRequest pageRequest) {
		List<Order> foundedList = findAllByUser(user).orElseThrow(
			() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		int start = (int)pageRequest.getOffset();
		int end = Math.min(start + pageRequest.getPageSize(), foundedList.size());
		return new PageImpl<>(foundedList.subList(start, end), pageRequest, foundedList.size());
	}
}
