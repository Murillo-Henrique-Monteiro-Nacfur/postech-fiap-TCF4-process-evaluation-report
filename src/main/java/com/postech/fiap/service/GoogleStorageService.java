package com.postech.fiap.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class GoogleStorageService {

    private final Storage storage;

    public GoogleStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public GoogleStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(String bucketName, String objectName, byte[] content, String contentType) {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        storage.create(blobInfo, content);

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }

}
