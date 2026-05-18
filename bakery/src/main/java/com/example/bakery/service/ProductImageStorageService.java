package com.example.bakery.service;

import com.example.bakery.dto.ProductImageUploadResponse;
import com.example.bakery.exceptions.InvalidProductImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductImageStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final Path productsUploadDir;

    public ProductImageStorageService(@Value("${app.upload.products-dir:/app/uploads/products}") String productsUploadDir) {
        this.productsUploadDir = Paths.get(productsUploadDir).toAbsolutePath().normalize();
    }

    public ProductImageUploadResponse storeProductImage(MultipartFile file) {
        validateFile(file);
        createUploadDirectoryIfNeeded();

        String extension = getExtension(file.getOriginalFilename());
        String generatedFilename = UUID.randomUUID() + "." + extension;
        Path targetPath = productsUploadDir.resolve(generatedFilename).normalize();

        if (!targetPath.startsWith(productsUploadDir)) {
            throw new InvalidProductImageException("Некорректное имя файла.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new InvalidProductImageException("Не удалось сохранить изображение товара.");
        }

        return new ProductImageUploadResponse("/uploads/products/" + generatedFilename);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidProductImageException("Файл изображения обязателен.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new InvalidProductImageException("Разрешены только файлы jpg, png и webp.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new InvalidProductImageException("Некорректный тип файла. Разрешены только jpg, png и webp.");
        }
    }

    private void createUploadDirectoryIfNeeded() {
        try {
            Files.createDirectories(productsUploadDir);
        } catch (IOException exception) {
            throw new InvalidProductImageException("Не удалось подготовить папку для изображений.");
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new InvalidProductImageException("Файл должен иметь расширение.");
        }

        return filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
