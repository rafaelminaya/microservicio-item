package com.formacionbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 * @RibbonClient
 * Configura el/los "Ribbon Client" que tengamos.
 * En este caso a la clase "ProductoClienteRest" anotada con "@FeignClient"
 * Debemos usar "@RibbonClients" en caso tuviesemos mas "Ribbon Client".
 * 
 * name = "servicio-productos : Especifica el microservicio al que se conectará
 * 
 * @EnableFeignClients
 * Habilita/permite el uso los "Feign client" que tengamos implementados en nuestro proyecto.
 * Además que permite inyectar nuestros "Feign client" en diversos componentes de spring(controllers, services, etc.)
 */
@RibbonClient(name = "servicio-productos")
@EnableFeignClients
@SpringBootApplication
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}

}
