package com.ssheld.onestopgifshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * Author: Stephen Sheldon
 **/
@SpringBootApplication
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    ApplicationContext appContext;

    public static void main(String[] args) {

        logger.info("Starting application!");
        SpringApplication.run(Application.class, args);
        logger.info("Application has started!");
    }
}
