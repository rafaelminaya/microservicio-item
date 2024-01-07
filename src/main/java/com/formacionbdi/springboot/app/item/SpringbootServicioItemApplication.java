package com.formacionbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 * @EnableEurekaClient
 * Indica que el proyecto actual es un "cliente" que será registrado en un "eureka server".
 * Ribbon ya viene implícito en Eureka, por lo que será removido tanto su anotación como su dependencia de la clase main y del pom.xml. 
 *
 * name = "servicio-productos : Especifica el microservicio al que se conectará
 * 
 * @EnableFeignClients
 * Habilita/permite el uso los "Feign client" que tengamos implementados en nuestro proyecto.
 * Además que permite inyectar nuestros "Feign client" en diversos componentes de spring(controllers, services, etc.)
 */
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}

}
