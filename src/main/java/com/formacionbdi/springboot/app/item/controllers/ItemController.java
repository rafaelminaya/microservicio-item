package com.formacionbdi.springboot.app.item.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
/*
 * @RefreshScope
 * Anotación que permite actualizar los componentes del contenedor de spring en tiempo real, sin reiniciar la aplicación.
 * Es decir, aquellas clases anotadas con @Component y sus derivados (@Service, @RestController, etc).
 * Esto también aplica a los atributos inyectados con @Value y @Autowired.
 * Este proecedimiento se realizará mediante un endpoint de Spring Actuator
 * Requiere instalar esta dependencia en el "pom.xml"
 */
@RefreshScope
@RestController
public class ItemController {
	
	private final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private Environment env;
	
	// Inyectamos esta dependencia, con el fin de a implementar el patrón "circuit breaker" usando anotaciones.
	// El cual es una alternativa programática.
	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;
	
	// Esta propiedad se encuentra en el Repositorio Git del "servidor de configuraciones"
	@Value(value = "${configuracion.texto}")
	private String texto;

	@Autowired
	@Qualifier("serviceFeign")
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
	//@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		/*
		 * Implementación de resiliencia con la forma programática(programación funcional)
		 * .create("items")
		 * Aca creamos un nuevo circuito llamado "items".		
		 * Este nombre sera un identificacdor, para identificar al "circuit breaker" que vamos a configurar/implementar
		 * 
		 * .run()
		 * Método que contiene una expresion lambda.
		 * 1° Argumento:
		 * Contiene la comunicación hacia el microservicio con el que se intentará comunicar.
		 * Acá implementaremos la lógica en caso de fallos, donde ser verán los estados del circuit breaker.
		 * 2° Argumento:
		 * Contiene la excepción en caso falle la comunicación hacia el microservicio.
		 * Se emite un argumento que corresponde a una excepción, el cual es una instancia de "Throwable"
		 */
		return this.circuitBreakerFactory
				.create("items")
				.run(
						() -> this.itemService.findById(id, cantidad), 
						e -> metodoAlternativo(id, cantidad, e)
						);
	
	}
	
	// Anotación que implementa un circuit breaker con Resilience4j. 
	// La configuración que se usará será únicamente con el archivo de propiedades ".properties" o ".yml".
	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad) {
		return this.itemService.findById(id, cantidad);	
	}
	/*
	 * Configuración tanto del "circuit breaker" como del "TimeOut" a este endpoint.
	 * Para que funcionen ambas anotaciones, se necesitará remover el argumento "fallbackMethod" del "@TimeLimiter"
	 * 
	 * @TimeLimiter
	 * Anotación para configurar el "TimeOut" o "tiempo máximo de espera" para la request de este endpoint
	 * Esta anotación requiere que el método devuelva un tipo "CompletableFuture"
	 * Tiene como método alternativo a "metodoAlternativo2()"
	 * 
	 * @CircuitBreaker
	 * Agregamos el circuit breaker a este endpoint. 
	 * Tiene como método alternativo a "metodoAlternativo2()"
	 * 
	 */
	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2")
	@TimeLimiter(name = "items")	
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad) {
		return CompletableFuture.supplyAsync(() ->  this.itemService.findById(id, cantidad));	
	}
	
	
	/*
	 * Método alternativo con una firma igual al método "detalle()"
	 * Este método podría incluso, comunicarse con otra instancia de un microservicio,
	 * sea con Feign o RestTemplate.
	 */
	public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
		
		logger.info(e.getMessage());
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
	
	// Método alternativo correspondiente al método "detalle3()"
	public CompletableFuture<Item> metodoAlternativo2(Long id, Integer cantidad, Throwable e) {
		
		logger.info(e.getMessage());
		Item item = new Item();
		Producto producto = new Producto();
		
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Camara Sony");
		producto.setPrecio(5000);
		producto.setCreateAt(new Date());
		item.setProducto(producto);
	
				
		return CompletableFuture.supplyAsync(() -> item);
	}
	
	// Endpoint que imprime las propiedades configuaradas en nuestro "servidor de configuraciones"
	// El argumento corresponde a una "inyección de dependencias" por argumento en un método.
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfig(
			@Value(value = "${server.port}") String puerto) {
		
		logger.info("Config server - Texto: " + this.texto);
		logger.info("Config server - Puerto: " + puerto);
		
		Map<String, String> json = new HashMap<>();		
		json.put("texto", this.texto);
		json.put("puerto", puerto);
		
		// Validación de que existe algún "profile" y que este sea igual a "dev"
		if(env.getActiveProfiles().length > 0 && this.env.getActiveProfiles()[0].equals("dev")) {
			// Agregamos los las propiedades del profile "dev" al Hash
			json.put("autor.nombre", this.env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", this.env.getProperty("configuracion.autor.email"));
		}				
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}
	

}
