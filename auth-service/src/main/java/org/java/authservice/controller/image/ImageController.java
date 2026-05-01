package org.java.authservice.controller.image;

import org.java.authservice.model.response.ImageResponse;
import org.java.authservice.service.s3.S3Service;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ImageResponse response = s3Service.uploadFile(file);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "Failed to upload image"));
        }
    }
}
