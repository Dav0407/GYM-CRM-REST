package com.epam.gym_crm;

import com.epam.gym_crm.config.ApplicationConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class Application {

    private static final Logger LOG = LogManager.getLogger(Application.class);

    public static void main(String[] args) throws LifecycleException {

        System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "info");
        LOG.info("Setting up the application...");
        // Create Spring context
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);

        LOG.info("Setting up Tomcat...");
        // Setup Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        String docBase = new File("src/main/webapp").getAbsolutePath();
        Context tomcatContext = tomcat.addContext("", docBase);

        // Add Spring DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        Tomcat.addServlet(tomcatContext, "dispatcherServlet", dispatcherServlet);
        tomcatContext.addServletMappingDecoded("/*", "dispatcherServlet");

        // Start Tomcat
        tomcat.start();
        LOG.info("Tomcat started at port: {}", tomcat.getConnector().getLocalPort());
        tomcat.getServer().await();
    }
}