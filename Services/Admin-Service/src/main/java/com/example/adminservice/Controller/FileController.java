package com.example.adminservice.Controller;

import com.example.adminservice.Services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final ModuleService moduleService;

    @GetMapping("/icons/{filename}")
    public ResponseEntity<byte[]> getIcon(@PathVariable String filename) {
        try {
            byte[] fileContent = moduleService.getIconFile(filename);

            HttpHeaders headers = new HttpHeaders();
            // Déterminer le type de contenu basé sur l'extension
            if (filename.toLowerCase().endsWith(".png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (filename.toLowerCase().endsWith(".gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}