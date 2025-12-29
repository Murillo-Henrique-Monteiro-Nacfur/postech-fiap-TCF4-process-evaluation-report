package com.postech.fiap.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class EmailRequestDto {
    private List<String> destinations;
    private String message;
    private String subject;
    private AttachmentDto attachment;

    public EmailRequestDto() {
    }

    public EmailRequestDto(List<String> destinations, String message, String subject, AttachmentDto attachment) {
        this.destinations = destinations;
        this.message = message;
        this.subject = subject;
        this.attachment = attachment;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AttachmentDto getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentDto attachment) {
        this.attachment = attachment;
    }
}
