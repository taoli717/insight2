package com.insight.web.config;

import javax.servlet.ServletContext;  
import javax.servlet.ServletException;  
import javax.servlet.ServletRegistration.Dynamic;  
  
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;  
  
public class WebInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {  
          
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();  
        ctx.register(Config.class);
       // servletContext.addListener(new ContextLoaderListener(new AnnotationConfigWebApplicationContext()));
        ctx.setServletContext(servletContext);
       // servletContext.addListener(new ContextLoaderListener(ctx));
        Dynamic servlet = servletContext.addServlet("servlet", new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);  
          
    }  

}
