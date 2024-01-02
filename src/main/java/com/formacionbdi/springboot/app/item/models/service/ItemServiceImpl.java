package com.formacionbdi.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements ItemService {

	// ATRIBUTOS
	@Autowired
	private RestTemplate clienteRest;

	// MÉTODOS
	@Override
	public List<Item> findAll() {
		/*
		 * getForObject() 
		 * Primer argumento: 
		 * Es el endpoint del otro proyecto de donde se obtendrán los datos. 
		 * Con balanceo de carga reemplazaremos la IP y el puerto por el nombre del servicio a consumir, 
		 * ya que este se obtendrá automáticamente según la mejor instancia disponible.
		 * servicio-productos : Esto ha sido configurado con Ribbon(indicando los IPs y puertos) dentro del "application.properties" 
		 * 
		 * Pasaremos de esto: http://localhost:8001/listar a http://servicio-productos/listar
		 * 
		 * Segundo argumento: 
		 * Es el tipo de dato a recibir de este endpoint.
		 * 
		 * Arrays.asList() : Convertimos el array recibido al tipo "List".
		 * Producto[].class : Indicamos que recibiremos la información de tipo array Producto.
		 */
		List<Producto> productos = Arrays
				.asList(this.clienteRest.getForObject("http://servicio-productos/listar", Producto[].class));
		/*
		 * Transformamos el List de "productos" al tipo List de "item"
		 */
		return productos
				.stream()
				.map(producto -> new Item(producto, 1))
				.collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {

		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		// {id} : Es un "path variable" cuyo valor será reemplazado por la variable del tipo "Map" llamada "pathVariables" 
		Producto producto = this.clienteRest.getForObject("http://servicio-productos/ver/{id}", Producto.class, pathVariables);

		return new Item(producto, cantidad);
	}

}
