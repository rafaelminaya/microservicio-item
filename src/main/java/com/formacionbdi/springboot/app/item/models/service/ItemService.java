package com.formacionbdi.springboot.app.item.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.item.models.Item;

public interface ItemService {
	
	List<Item> findAll();

	Item findById(Long id, Integer cantidad);
}
