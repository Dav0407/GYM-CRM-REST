package com.epam.gym_crm.config.log_config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class TransactionLoggingFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(TransactionLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            String transactionId = UUID.randomUUID().toString();
            ThreadContext.put("transactionId", transactionId);

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            LOG.info("Transaction started: {} for {} {}", transactionId, httpRequest.getMethod(), httpRequest.getRequestURI());
            System.out.println("Transaction started: " + transactionId);
            chain.doFilter(request, response);
        } finally {
            ThreadContext.clearMap();
        }
    }
}
