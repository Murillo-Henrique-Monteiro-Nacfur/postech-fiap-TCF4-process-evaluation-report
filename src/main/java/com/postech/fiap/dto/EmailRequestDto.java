package com.postech.fiap.dto;

import org.hibernate.internal.build.AllowReflection;

import java.util.List;

@AllowReflection
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
