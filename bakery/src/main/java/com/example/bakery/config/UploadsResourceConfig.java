package com.example.bakery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UploadsResourceConfig implements WebMvcConfigurer {

    private final String uploadsRootDir;

    public UploadsResourceConfig(@Value("${app.upload.root-dir:/app/uploads}") String uploadsRootDir) {
        this.uploadsRootDir = uploadsRootDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadsRootDir).toAbsolutePath().normalize();
        String resourceLocation = uploadPath.toUri().toString();
        if (!resourceLocation.endsWith("/")) {
            resourceLocation += "/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
