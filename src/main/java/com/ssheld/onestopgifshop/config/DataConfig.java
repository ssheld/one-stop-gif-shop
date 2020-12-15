package com.ssheld.onestopgifshop.config;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

/**
 * Author: Stephen Sheldon
 **/
@Configuration
@ComponentScan(basePackages = {"com.ssheld"})
@PropertySource("classpath:db/jdbc.properties")
public class DataConfig {
    @Autowired
    private Environment env;

    // Pull values from jdbc.properties file while providing
    // a default implementation in the case property can't be found.
    @Value("${onestopgifshop.db.driver}")
    private String driverClassName;
    @Value("${onestopgifshop.db.url}")
    private String url;
    @Value("${onestopgifshop.db.username}")
    private String username;
    @Value("${onestopgifshop.db.password}")
    private String password;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        Resource config = new ClassPathResource("hibernate.cfg.xml");
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setConfigLocation(config);
        sessionFactory.setPackagesToScan(env.getProperty("onestopgifshop.entity.package"));
        sessionFactory.setDataSource(dataSource());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();

        // Driver class name
        ds.setDriverClassName(env.getProperty("onestopgifshop.db.driver"));

        // Set URL
        ds.setUrl(env.getProperty("onestopgifshop.db.url"));

        // Set username & password
        ds.setUsername(env.getProperty("onestopgifshop.db.username"));
        ds.setPassword(env.getProperty("onestopgifshop.db.password"));

        return ds;
    }
}

