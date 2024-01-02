package com.formacionbdi.springboot.app.item;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/*
 * Configuraremos el RestTemplate que es un "web client".
 * Además lo registraremos como un componente de Spring.
 * Y así poder inyectarlo en la clase de servicio "ItemServiceImpl".
 */
@Configuration
public class RestTemplateConfig {
    /*
     * Una clase anotada con @Configuration permite crear objetos y registrarlos en el contenedor de Spring
     * por medio de métodos usando la anotación @Bean.
     * De no indicar el nombre del bean, este por defecto será el nombre del método.
     * 
     * RestTemplate 
     * Es un "web client" HTTP para trabajar con API Rest.
     * Para poder acceder a recursos de otros micro servicios.
     * 
     * @LoadBalanced
     * Permite utilizar Ribbon para el balanceo de carga, para evitar el indicar los IPs y puertos de los endpoints a utilizar.
     * Y con RestTemplate por debajo utilizando el balanceador buscando la mejor instancia disponible.
     */
    @Bean("clienteRestTemplate")
    @LoadBalanced
    RestTemplate registarRestTemplate() {
		return new RestTemplate();
	}

}
