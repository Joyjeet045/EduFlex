package com.example.my_project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

@Controller
public class FileController {

    // Root path where files are stored
    private static final String ROOT_PATH = "/"; // This represents the system root

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) {
        try {
            // Remove any leading slash if present to ensure proper path construction
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }

            // Construct the absolute path starting from root
            Path path = Paths.get(ROOT_PATH, filePath);
            
            // Validate the file exists
            File file = path.toFile();
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Security check for path traversal
            String normalizedPath = path.normalize().toString();
            if (!normalizedPath.startsWith(ROOT_PATH)) {
                return ResponseEntity.badRequest().build();
            }

            // Create a Resource from the file
            Resource resource = new UrlResource(file.toURI());

            // Determine content type
            String contentType = null;
            try {
                contentType = Files.probeContentType(path);
            } catch (IOException ex) {
                contentType = "application/octet-stream";
            }

            // Get just the filename for the Content-Disposition header
            String fileName = path.getFileName().toString();

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper method to validate paths
    private boolean isValidPath(Path path) {
        try {
            // Normalize the path and ensure it doesn't contain any ../ sequences
            String normalizedPath = path.normalize().toString();
            return !normalizedPath.contains("..");
        } catch (SecurityException e) {
            return false;
        }
    }
}
