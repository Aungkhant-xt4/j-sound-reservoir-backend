package org.java.commonlibrary.model.dto.media;

import org.java.commonlibrary.model.enumuration.MediaCategory;

import java.util.Map;

public record PresignedUploadRequest(
        String fileName,
        String contentType,
        long contentLength,
        MediaCategory category,
        Map<String, String> metadata
) {}
