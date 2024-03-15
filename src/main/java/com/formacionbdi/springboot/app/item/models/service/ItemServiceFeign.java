package com.formacionbdi.springboot.app.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.app.item.clientes.ProductoClienteRest;
import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.commons.models.entity.Producto;

// Esta clase es similar a "ItemServiceImpl" pero en este caso usando un "client Feign"
@Service("serviceFeign")
public class ItemServiceFeign implements ItemService {

	// ATRIBUTOS
	@Autowired
	private ProductoClienteRest clienteFeign;

	// MÉTODOS
	@Override
	public List<Item> findAll() {
		return  this.clienteFeign.listar()
				.stream()
				.map(producto -> new Item(producto, 1))
				.collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		return new Item(this.clienteFeign.detalle(id), cantidad); 
	}

	@Override
	public Producto save(Producto producto) {		
		return this.clienteFeign.crear(producto);
	}

	@Override
	public Producto update(Producto producto, Long id) {		
		return this.clienteFeign.editar(producto, id);
	}

	@Override
	public void deleteById(Long id) {
		this.clienteFeign.eliminar(id);
		
	}

}
