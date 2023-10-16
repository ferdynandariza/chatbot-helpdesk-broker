package com.iglo.chatbothelpdesk.model.blasting;

import lombok.Data;

import java.util.List;

@Data
public class BlastingWaBody {
    private String name;
    private String channel;
    private String sendingMode;
    private String templateId;
    private List<Object> headerVariables;
    private List<Object> bodyVariables;
    private List<Object> content;
    private List<Object> whatsappRecipient;
}
