package com.example.outsourcingproject.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcingproject.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
