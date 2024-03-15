package com.formacionbdi.springboot.app.item.models;

import com.formacionbdi.springboot.app.commons.models.entity.Producto;

public class Item {
	// ATRIBUTOS
	private Producto producto;
	private Integer cantidad;

	// CONSTRUCTORES
	public Item() {

	}

	public Item(Producto producto, Integer cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
	}

	// MÉTODOS
	public Double getTotal() {
		return producto.getPrecio() * cantidad.doubleValue();
	}

	// GETTERS AND SETTERS
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
