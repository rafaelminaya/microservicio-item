package com.formacionbdi.springboot.app.item.models;

import java.util.Date;

// En esta clase, replicaremos en forma a la clase "Producto" del proyecto "springboot-servicio-producto"
public class Producto {
	// ATRIBUTOS
	private Long id;
	private String nombre;
	private Double precio;
	private Date createAt;
	private Integer port;

	// GETTERS AND SETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
