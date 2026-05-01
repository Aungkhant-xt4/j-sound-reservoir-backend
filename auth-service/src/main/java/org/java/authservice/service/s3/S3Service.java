package org.java.authservice.service.s3;

import lombok.RequiredArgsConstructor;
import org.java.authservice.model.response.ImageResponse;
import org.java.authservice.model.response.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;
    public S3Service(S3Client s3Client, @Value("${s3.bucketName}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public ImageResponse uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String guidName = UUID.randomUUID().toString() + extension;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(guidName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        return new ImageResponse(guidName);
    }
}
