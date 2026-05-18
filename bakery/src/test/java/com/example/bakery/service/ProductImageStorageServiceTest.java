package com.example.bakery.service;

import com.example.bakery.dto.ProductImageUploadResponse;
import com.example.bakery.exceptions.InvalidProductImageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductImageStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void storeProductImageSavesAllowedFileAndReturnsPublicPath() throws IOException {
        ProductImageStorageService storageService = new ProductImageStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "bun.png",
                "image/png",
                "png-data".getBytes()
        );

        ProductImageUploadResponse response = storageService.storeProductImage(file);

        assertTrue(response.getImagePath().startsWith("/uploads/products/"));
        String storedFilename = response.getImagePath().substring("/uploads/products/".length());
        assertTrue(Files.exists(tempDir.resolve(storedFilename)));
    }

    @Test
    void storeProductImageRejectsUnsupportedExtension() {
        ProductImageStorageService storageService = new ProductImageStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "bun.gif",
                "image/gif",
                "gif-data".getBytes()
        );

        assertThrows(InvalidProductImageException.class, () -> storageService.storeProductImage(file));
    }
}
