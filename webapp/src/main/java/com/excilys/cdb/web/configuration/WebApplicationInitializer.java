package com.excilys.cdb.web.configuration;

import com.excilys.cdb.persistence.configuration.PersistenceConfiguration;
import com.excilys.cdb.service.configuration.ServiceConfiguration;
import com.excilys.cdb.shared.configuration.SharedConfiguration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebApplicationInitializer implements org.springframework.web.WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
	// Create the 'root' Spring application context
	AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(WebConfiguration.class, SharedConfiguration.class, ServiceConfiguration.class, PersistenceConfiguration.class, WebSecurityConfiguration.class);

	// Manage the lifecycle of the root application context
	container.addListener(new ContextLoaderListener(rootContext));
	// Create the dispatcher servlet's Spring application context
	AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
	dispatcherContext.register(WebMvcConfiguration.class);

	// Register and map the dispatcher servlet
	ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher",
		new DispatcherServlet(dispatcherContext));
	dispatcher.setLoadOnStartup(1);
	dispatcher.addMapping("/");

    }

}