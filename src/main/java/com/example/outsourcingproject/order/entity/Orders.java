package com.example.outsourcingproject.order.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.outsourcingproject.common.BaseEntity;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.order.enums.OrderStatus;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
public class Orders extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	private OrderStatus orderStatus;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Menu menu;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Store store;

	@NotNull
	private Long quantity;

	@NotBlank
	private String address;

	@Builder
	public Orders(User user, Menu menu, Store store, Long quantity, String address, OrderStatus orderStatus) {
		this.user = user;
		this.menu = menu;
		this.store = store;
		this.quantity = quantity;
		this.address = address;
		this.orderStatus = orderStatus;
	}

	public void chageStatus(String orderStatus) {
		this.orderStatus = OrderStatus.of(orderStatus);
	}
}
