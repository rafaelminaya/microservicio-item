package com.formacionbdi.springboot.app.item.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;


public interface ItemService {
	
	List<Item> findAll();

	Item findById(Long id, Integer cantidad);
	
		
	Producto save(Producto producto);
	
	Producto update(Producto producto, Long id);
	
	void deleteById(Long id);
}
