package com.epam.gym_crm;

import com.epam.gym_crm.config.ApplicationConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class Application {

    public static void main(String[] args) throws LifecycleException {
        // Create Spring context
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);

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
        System.out.println("Server started on port 8080");
        tomcat.getServer().await();
    }
}