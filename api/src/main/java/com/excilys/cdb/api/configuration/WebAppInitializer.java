package com.excilys.cdb.api.configuration;

import com.excilys.cdb.persistence.configuration.PersistenceConfiguration;
import com.excilys.cdb.security.configuration.SecurityConfiguration;
import com.excilys.cdb.service.configuration.ServiceConfiguration;
import com.excilys.cdb.shared.configuration.SharedConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RestConfiguration.class, SharedConfiguration.class, ServiceConfiguration.class,
                PersistenceConfiguration.class, SecurityConfiguration.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Register and map the mvc dispatcher servlet
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebMvcConfiguration.class);

        final DispatcherServlet servlet = new DispatcherServlet(dispatcherContext);
        servlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", servlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
