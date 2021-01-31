package com.ssheld.onestopgifshop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

/**
 * Author: Stephen Sheldon
 **/
@Service("beansPrinter")
public class BeansPrinter {

    private static Logger logger = LoggerFactory.getLogger(BeansPrinter.class);

    @Autowired
    private ApplicationContext appContext;

    public BeansPrinter() {

    }

    public void init() {
        try {
            SpringSecurityDialect dialect = appContext.getBean(SpringSecurityDialect.class);
            logger.info("Spring Security Dialect exist");
        } catch (NoSuchBeanDefinitionException e) {
            logger.error("Spring Security Dialect doesn't exist");
        }
    }

    public void printBeans()
    {
        System.out.println("Number of beans:");
        System.out.println(appContext.getBeanDefinitionCount());

        String[] names = appContext.getBeanDefinitionNames();
        for(String name : names)
        {
            System.out.println("-----------------");
            System.out.println(name);
        }
    }
}
