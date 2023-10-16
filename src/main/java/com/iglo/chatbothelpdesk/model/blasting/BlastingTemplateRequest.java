package com.iglo.chatbothelpdesk.model.blasting;

import lombok.Data;

import java.util.List;

@Data
public class BlastingTemplateRequest {
    private String name;

    private String channel;

    private String templateId;

    private String phone;

    private List<Object> fields;
}
