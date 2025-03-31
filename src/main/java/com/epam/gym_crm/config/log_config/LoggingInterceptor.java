package com.epam.gym_crm.config.log_config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LogManager.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String transactionId = ThreadContext.get("transactionId"); // Retrieve transactionId

        LOG.info("Transaction: {} - Incoming Request: {} {} - Headers: {}",
                transactionId, request.getMethod(), request.getRequestURI(), request.getHeaderNames());

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex) {
        String transactionId = ThreadContext.get("transactionId");

        LOG.info("Transaction: {} - Response Status: {} - Error: {}",
                transactionId, response.getStatus(), ex != null ? ex.getMessage() : "None");
    }
}
