package org.java.commonlibrary.service.media.impl;

import lombok.RequiredArgsConstructor;
import org.java.commonlibrary.model.dto.media.PresignedUpload;
import org.java.commonlibrary.model.dto.media.PresignedUploadRequest;
import org.java.commonlibrary.model.enumuration.*;
import org.java.commonlibrary.service.media.MediaStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3MediaStorage implements MediaStorage {

    private final S3Presigner presigner;

    @Value("${s3.bucketName}")
    private String bucketName;

    @Override
    public PresignedUpload generatePresignedUploadUrl(
            PresignedUploadRequest request) {

        String key = UUID.randomUUID() + "-" + request.fileName();

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(request.contentType())
                        .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(15))
                        .putObjectRequest(putObjectRequest)
                        .build();

        PresignedPutObjectRequest presigned =
                presigner.presignPutObject(presignRequest);

        return new PresignedUpload(
                presigned.url().toString(),
                key,
                Instant.now().plus(Duration.ofMinutes(15)),
                Map.of(
                        "Content-Type",
                        request.contentType()
                )
        );
    }

    @Override
    public String getType() {
        return MediaStorageType.S3.toString();
    }
}
