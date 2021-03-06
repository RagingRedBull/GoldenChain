package com.remd.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remd.spring.model.ItemCategory;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Integer> {

}
