package com.obakeng.cruddemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

  @Value("${obakeng.openapi.local-url}")
  private String devUrl;


  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Local environment");

    Contact contact = new Contact();
    contact.setEmail("nchupetsang.obk@gmail.com");
    contact.setName("Obakeng");

    License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    Info info = new Info()
        .title("Demo Management API")
        .version("1.0")
        .contact(contact)
        .description("This API exposes endpoints for the Demo")
        .license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}
