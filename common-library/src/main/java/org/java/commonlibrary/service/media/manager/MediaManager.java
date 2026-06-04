package org.java.commonlibrary.service.media.manager;

import org.java.commonlibrary.model.dto.media.PresignedUpload;
import org.java.commonlibrary.model.dto.media.PresignedUploadRequest;
import org.java.commonlibrary.service.media.MediaStorage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MediaManager {
    private final Map<String, MediaStorage> storages = new HashMap<>();

    public MediaManager(List<MediaStorage> mediaStorages) {
        for (MediaStorage storage : mediaStorages) {
            storages.put(storage.getType(), storage);
        }
    }

    public PresignedUpload getPresignedUrlVia(String type, PresignedUploadRequest request) {
        MediaStorage storage = storages.get(type.toLowerCase());
        return storage.generatePresignedUploadUrl(request);
    }
}