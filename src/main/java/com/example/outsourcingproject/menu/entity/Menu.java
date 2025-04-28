package com.example.outsourcingproject.menu.entity;

import com.example.outsourcingproject.common.BaseEntity;
import com.example.outsourcingproject.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Long menuId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store storeId;

	@Column(nullable = false)
	private String menuName;

	@Column(nullable = false)
	private Long menuPrice;

	@Builder.Default
	private String discription = "";

	@Builder.Default
	private boolean isDeleted = false;

	public void updateMenu(String menuName, Long menuPrice, String discription) {
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.discription = discription;
	}

	public void deletetMenu(String menuName, long menuPrice, boolean isDeleted) {
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.isDeleted = isDeleted;
	}
}
