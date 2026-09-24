package com.resumebuilder.config;

import com.resumebuilder.filter.AdminInterceptor;
import com.resumebuilder.filter.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/dashboard/**", "/profile/**", "/education/**",
                        "/experience/**", "/skills/**", "/certifications/**",
                        "/projects/**", "/achievements/**", "/resume/**", "/admin/**");

        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**");
    }
}
