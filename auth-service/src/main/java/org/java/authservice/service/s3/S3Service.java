package org.java.authservice.service.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.authservice.model.response.ImageResponse;
import org.java.authservice.model.response.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
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

        try {
            // Capture the response object
            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()));

            // Double-check the HTTP status code from AWS
            if (!response.sdkHttpResponse().isSuccessful()) {
                throw new RuntimeException("S3 rejected request with status code: "
                        + response.sdkHttpResponse().statusCode());
            }

            log.info("Successfully uploaded {} to S3. ETag: {}", guidName, response.eTag());
            return new ImageResponse(guidName);

        } catch (S3Exception e) {
            // Credentials incorrect, bucket doesn't exist, or ACL issues
            log.error("AWS S3 Service Error [{}]: {}", e.awsErrorDetails().errorCode(), e.getMessage());
            throw new RuntimeException("Storage service failure: " + e.awsErrorDetails().errorMessage(), e);

        } catch (SdkClientException e) {
            // Network timeouts, connection drops, or DNS resolution issues
            log.error("AWS SDK Client Error (Possible Timeout/Network Drop): {}", e.getMessage());
            throw new RuntimeException("Failed to reach storage network", e);
        }
    }
}
