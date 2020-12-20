package com.ssheld.onestopgifshop.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Author: Stephen Sheldon
 **/
@Configuration
@PropertySource("classpath:db/s3.properties")
public class S3Config {

    @Bean
    public AmazonS3 amazons3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .build();
    }
}
