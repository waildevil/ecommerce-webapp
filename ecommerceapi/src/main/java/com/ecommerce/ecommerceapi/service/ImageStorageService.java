package com.ecommerce.ecommerceapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    private final Path rootLocation;

    public ImageStorageService() {

        this.rootLocation = Paths.get("src/main/resources/static/images");
    }

    public String storeImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot store empty file.");
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
                .normalize().toAbsolutePath();


        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new IllegalStateException("Cannot store file outside current directory.");
        }


        Files.createDirectories(destinationFile.getParent());


        file.transferTo(destinationFile);

        return "/images/" + filename;
    }
}
