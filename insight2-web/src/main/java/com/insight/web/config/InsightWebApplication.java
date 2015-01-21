package com.insight.web.config;

/**
 * Created by PC on 1/21/2015.
 */
import generator.config.DataGeneratorAppConfig;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@EnableAutoConfiguration
@Configuration
//@PropertySource("classpath:/app-config.properties")
public class InsightWebApplication  extends SpringBootServletInitializer {
    private static Class<Application> applicationClass = Application.class;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    public static void main(String[] args) {
       new SpringApplicationBuilder().headless(true)
               .sources(InsightWebApplication.class, InsightWebMvcConfig.class)
               .headless(true).web(true).run(args);
    }

}