package org.java.commonlibrary.model.dto.media;

import java.time.Instant;
import java.util.Map;

public record PresignedUpload(
        String uploadUrl,
        String mediaKey,
        Instant expiresAt,
        Map<String, String> requiredHeaders
) {}