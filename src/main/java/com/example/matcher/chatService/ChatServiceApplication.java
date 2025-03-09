package com.example.matcher.chatService;

import com.example.matcher.chatService.tools.EnvironmentLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableDiscoveryClient
public class ChatServiceApplication {
	public static void main(String[] args) {
		System.setProperty("spring.datasource.url", EnvironmentLoader.get("SPRING_DATASOURCE_URL"));
		System.setProperty("spring.datasource.username", EnvironmentLoader.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("spring.datasource.password", EnvironmentLoader.get("SPRING_DATASOURCE_PASSWORD"));

		System.setProperty("aws.access.key", EnvironmentLoader.get("AWS_ACCESS_KEY"));
		System.setProperty("aws.secret.key", EnvironmentLoader.get("AWS_SECRET_KEY"));
		System.setProperty("aws.s3.my_domain", EnvironmentLoader.get("AWS_MY_DOMAIN"));
		System.setProperty("aws.s3.buck_name", EnvironmentLoader.get("AWS_BUCK_NAME"));

		System.setProperty("spring.kafka.bootstrap-servers", EnvironmentLoader.get("SPRING_KAFKA_BOOTSTRAP_SERVERS", "http://localhost:9092"));
		System.setProperty("eureka.client.service-url.defaultZone", EnvironmentLoader.get("EUREKA_DEFAULT_ZONE", "http://localhost:8761/eureka/"));
		System.setProperty("spring.jpa.hibernate.ddl-auto", EnvironmentLoader.get("SPRING_JPA_HIBERNATE_DDL_AUTO", "update"));

		System.setProperty("server.port", EnvironmentLoader.get("SERVER_PORT", "8081"));
		System.setProperty("server.address", EnvironmentLoader.get("SERVER_ADDRESS", "0.0.0.0"));

		System.out.println(System.getProperties());

		SpringApplication.run(ChatServiceApplication.class, args);
	}

}
