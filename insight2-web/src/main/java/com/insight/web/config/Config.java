package com.insight.web.config;

import generator.config.DataGeneratorAppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;  
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.JstlView;  
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration //Marks this class as configuration
//Specifies which package to scan
//@ComponentScan({"com.insight"})
@ComponentScan({"com.insight"})
@Import({ DataGeneratorAppConfig.class })
//Enables Spring's annotations
//@Import({MongoConfig.class})
@EnableWebMvc   
public class Config {  
      
    @Bean  
    public UrlBasedViewResolver setupViewResolver() {  
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();  
        resolver.setPrefix("/WEB-INF/views/");  
        resolver.setSuffix(".jsp");  
        resolver.setViewClass(JstlView.class);  
        return resolver;  
    }  
  
}  
