package com.formacionbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
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
 * 
 * Para la versiones Spring Boot menores que 2.4*:
 * Utilizará la dependencia de "Ribbon" y la dependencia de "Hystrix"
 * 
 * Para versiones mayores a 2.4.*
 * Utilizará "Spring Load Balancer", el cual ya tiene incluido a Ribbon dentro de Eureka.
 * Y utilizará "Resilience4j" en reemplazo de "Hystrix".
 */

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
/*
 * Anotación necesaria para el H2 de la clase "Producto" del servicio "commons".
 * Permite evitar escribir las cadenas de conexión en el archivo "application.properties" del H2 para que tome la configuración por defecto
 */
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}

}
