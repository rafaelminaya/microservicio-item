package com.formacionbdi.springboot.app.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.formacionbdi.springboot.app.item.models.Producto;

/*
 * @FeignClient
 * Indica que esta interfaz representa a un "cliente Feign", el cual es un "web client".
 * Además que con esta anotación será registrado como un "bean" dentro del "contenedor de spring" y podrá ser inyectado como dependencia.
 * Será inyectado dentro de la clase servicio "ItemServiceFeign"
 * 
 * name = "servicio-productos"
 * Indicamos el nombre del microservicio al que nos queremos conectar
 * 
 * url = "localhost:8001"
 * Indicamos el IP y el puerto. localhost equivale a escribir 127.0.0.1
 * 
 * Desacoplaremos el IP y puerto del microservicio.
 * Configurando los IPs y puertos, pero con Ribbon, dentro del archivo "application.properties"
 * "servicio-productos" es el nombre del microservicio al que nos conectaremos y está especificado en el "application.properties" 
 */
//@FeignClient(name = "servicio-productos", url = "localhost:8001")
@FeignClient(name = "servicio-productos")
public interface ProductoClienteRest {
	// Utilizamos las mismas estructuras de los métodos endpoint que vamos a consumir.
	// Pero en este caso, siendo un cliente Feign, lo utilizamos para consumir los servicios rest.  
	// Así obtener los datos del JSON convertidos a nuestro objetos POJO.
	@GetMapping("/listar")
	public List<Producto> listar();
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id);
}
