package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * Hello world.
 *
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class App {


    /**
     * Logger.
     */
    private final Logger logger =
            LoggerFactory.getLogger(App.class);

    /**
     * Main method of this application.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }
    /**
     * This will be invoked one the application is started.
     *
     * @param event
     */
    @EventListener
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("Application Started at {}", event.getTimestamp());
    }
}
