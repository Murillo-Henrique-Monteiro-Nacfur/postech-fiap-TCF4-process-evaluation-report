package com.postech.fiap.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AttachmentDto {
    private String bucket;
    private String nameFile;

    public AttachmentDto() {
    }

    public AttachmentDto(String bucket, String nameFile) {
        this.bucket = bucket;
        this.nameFile = nameFile;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
}
