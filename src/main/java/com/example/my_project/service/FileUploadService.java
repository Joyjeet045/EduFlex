package com.example.my_project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    // Path to save files
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) {
        // Ensure the directory exists
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // Create the directory if it doesn't exist
        }

        try {
            // Create a unique file name to avoid conflicts
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // Save the file to the specified path
            file.transferTo(filePath.toFile());

            // Return the path or URL of the saved file
            return filePath.toString(); // Returns the absolute path; adjust if needed
        } catch (IOException e) {
            // Handle the exception
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
        }
    }
}
