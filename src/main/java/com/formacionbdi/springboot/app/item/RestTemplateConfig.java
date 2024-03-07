package com.formacionbdi.springboot.app.item;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

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
    
    // Método para configurar los valores por defecto de cada "circuit breaker" implementado en el proyecto.
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaulCustomizer() {
    	/* 
    	 * Se emite el argumento "factory" y le realizamos las configuraciones por defecto con el método "configureDefault()"
    	 * 
    	 * id:
    	 * Representa el identificador de cada de cada circuit breaker implementado.
    	 * Por ejemplo el circuit breaker de "items", indicado en el método "detalle()" del "ItemController"  
    	 */
    	return factory -> factory.configureDefault(id -> {
    		/* Este "id" se aplicará para cualquier "circuit breaker" que tengamos implemenado en la aplicación, por ejemplo el "items".
    		 * 
    		 * circuitBreakerConfig
    		 * configura el "circuit breaker" de la condición
    		 * 
    		 * CircuitBreakerConfig.custom()
    		 * Configura varias parámetros de todos los circuit breaker implementado
    		 * Como por ejemplo: slidingWindowSize(), failureRateThreshold()
    		 */
    		return new Resilience4JConfigBuilder(id)
    				.circuitBreakerConfig(
    						CircuitBreakerConfig.custom()
    						.slidingWindowSize(10) // Cantidad de peticiones para evaluar fallos durante el estado "cerrado".
    						.failureRateThreshold(50) // Umbral de fallo, por defecto es 50, referente al 50%
    						.waitDurationInOpenState(Duration.ofSeconds(10L)) // Tiempo de duración en el estado "abierto"
    						.permittedNumberOfCallsInHalfOpenState(5) // cantidad de llamdas en el estado "semi-abierto"
    						.slowCallRateThreshold(50) // Umbral de fallo, pero para llamadas lentas, por defecto es 100, referente al 100%
    						.slowCallDurationThreshold(Duration.ofSeconds(2L)) // Tiempo maximo de espera para llamadas lentas, por cada request
    						.build()
    						)
    				//.timeLimiterConfig(TimeLimiterConfig.ofDefaults()) // Timeout/tiempoLimite, aunque de momento lo dejaremos con las configuraciones por defecto
    				.timeLimiterConfig(
    						TimeLimiterConfig.custom()
    						// Tiempo máximo de espera a 6 segundos. Por defecto es 1 segundo.
    						// Indicamos 6 segundos, puesto que en el "ProductoController" indicamos un sleep de 5 segundos.
    						// De ser menor el tiempo, arrojara un error de "TimeLimiter" lo cual también es aceptable si se quiere esperar este error.
    						// Este tiene proridad por sobre la llamada lenta configurado con el metodo "slowCallDurationThreshold()"
    						.timeoutDuration(Duration.ofSeconds(6L))  
    						.build()
    						)
    				.build();
    	});
    }

}
