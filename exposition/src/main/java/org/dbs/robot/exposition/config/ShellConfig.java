package org.dbs.robot.exposition.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Spring Shell.
 * This class ensures that Spring Shell is properly configured to work alongside Spring Web.
 */
@Configuration
public class ShellConfig {

    /**
     * Bean to ensure Spring Shell is properly initialized.
     * This is needed to make Spring Shell work alongside Spring Web.
     * 
     * @return A string indicating that Spring Shell is initialized
     */
    @Bean
    @ConditionalOnProperty(name = "spring.shell.interactive.enabled", havingValue = "true", matchIfMissing = true)
    public String shellInitializer() {
        return "Spring Shell initialized";
    }
}
