package org.java.commonlibrary.service.media;

import org.java.commonlibrary.model.dto.media.PresignedUpload;
import org.java.commonlibrary.model.dto.media.PresignedUploadRequest;

public interface MediaStorage {
    PresignedUpload generatePresignedUploadUrl(PresignedUploadRequest request);
    String getType();
}
