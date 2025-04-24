package com.example.outsourcingproject.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.outsourcingproject.menu.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	@Query("select m from Menu m where m.storeId= :id")
	List<Menu> findByStoreId(
		@Param("id") Long id);
}
