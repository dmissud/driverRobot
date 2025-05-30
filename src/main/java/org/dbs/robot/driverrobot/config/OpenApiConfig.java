package org.dbs.robot.driverrobot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation for the application.
     *
     * @return The OpenAPI configuration
     */
    @Bean
    public OpenAPI arduinoControllerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Arduino Controller API")
                        .description("API for controlling Arduino devices with LEDs and servomotors")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Arduino Controller Team")
                                .email("support@arduinocontroller.org")
                                .url("https://arduinocontroller.org"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL")
                ));
    }
}