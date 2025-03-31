package com.epam.gym_crm.config;

import com.epam.gym_crm.config.log_config.TransactionLoggingFilter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;

public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        FilterRegistration.Dynamic transactionLoggingFilter = servletContext.addFilter("transactionLoggingFilter", new TransactionLoggingFilter());
        transactionLoggingFilter.addMappingForUrlPatterns(null, false, "/*"); // Apply to all URLs
    }
}
