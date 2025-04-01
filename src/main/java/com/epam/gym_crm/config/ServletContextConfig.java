package com.epam.gym_crm.config;

import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.context.ServletContextAware;

@Configuration
public class ServletContextConfig implements ServletContextAware {

    private ServletContext servletContext;

    @Override
    public void setServletContext(@NonNull ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Bean
    public ServletContext servletContext() {
        return servletContext;
    }
}