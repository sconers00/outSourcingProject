package com.example.outsourcingproject.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcingproject.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
