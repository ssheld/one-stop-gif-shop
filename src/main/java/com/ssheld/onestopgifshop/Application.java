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
    private ApplicationContext appContext;

    public static void main(String[] args) {
        try {
            org.h2.tools.Server.createTcpServer().start();
        } catch (Exception e) {
            System.out.println("Could not start H2 database");
        }
        logger.info("Starting application!");
//        SpringApplication.run(OneStopGifShopApplication.class, args);
        SpringApplication.run(Application.class, args);
        logger.info("Application has started!");
    }

//    public void run(String... args) throws Exception {
//
//        String[] beans = appContext.getBeanDefinitionNames();
//        Arrays.sort(beans);
//        for (String bean : beans) {
//            logger.info(bean.toString());
//        }
//    }
}
