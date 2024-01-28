package com.formacionbdi.springboot.app.item.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ItemController {

	@Autowired
	@Qualifier("serviceRestTemplate")
	private ItemService itemService;

	/*
	 * @RequestParam(name = "nombre") String nombre:
	 * Corresponde al "param" recibido por el "filter" del "API Gateway" 
	 * Configurado en el archivo "application.yml" con "- AddRequestParameter=nombre, rafael"
	 * en el proyecto "springboot-sevicio-gatewaty-server" 
	 * 
	 * @RequestHeader(name = "token-request") String header
	 * Corresponde al "header" recibido por el "filter" del "API Gateway" 
	 * Configurado en el archivo "application.yml" con "AddRequestHeader=token-request, 123456"
	 * en el proyecto "springboot-sevicio-gatewaty-server" 
	 * 
	 */
	@GetMapping("/listar")
	public List<Item> listar(
			@RequestParam(name = "nombre", required = false) String nombre, 
			@RequestHeader(name = "token-request", required = false) String header) {
		
		System.out.println("Filtro Params - nombre: " + nombre);
		System.out.println("Filtro Header - token-request: " + header);
		
		return this.itemService.findAll();
	}

	/*
	 * @HystrixCommand
	 * Anotación para configurar la tolerancia a fallos con Hystrix
	 * 
	 * fallbackMethod = "metodoAlternativo"
	 * En este atributo manejamos la "tolerancia a fallos".
	 * Por lo que si falla la comunicación, estaríamos haciendo un "corto circuito", 
	 * dando un camino alternativo hacia el método llamado "metodoAlternativo()".
	 * El método alternativo debe tener la misma firma/estructura del método del método del controlador actual
	 */
	@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return this.itemService.findById(id, cantidad);
	}
	
	/*
	 * Método de firma igual al método "detalle"
	 * Este método podría incluso, comunicarse con otra instancia de un microservicio,
	 * sea con Feign o RestTemplate.
	 */
	public Item metodoAlternativo(Long id, Integer cantidad){
		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Camara Sony");
		producto.setPrecio(5000);
		producto.setCreateAt(new Date());
		item.setProducto(producto);
	
				
		return item;
	}
	

}
