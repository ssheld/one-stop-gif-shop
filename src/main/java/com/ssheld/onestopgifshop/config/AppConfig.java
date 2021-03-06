package com.ssheld.onestopgifshop.config;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Author: Stephen Sheldon
 **/
@Configuration
@PropertySource("classpath:app.properties")
public class AppConfig {
    @Autowired
    private Environment env;

    @Value("onestopgifshop.hash.salt")
    private String hashSalt;

    @Bean
    public Hashids hashids() {
        return new Hashids(env.getProperty("onestopgifshop.hash.salt"),8);
    }
}

