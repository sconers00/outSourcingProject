package com.example.outsourcingproject.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.outsourcingproject.menu.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
