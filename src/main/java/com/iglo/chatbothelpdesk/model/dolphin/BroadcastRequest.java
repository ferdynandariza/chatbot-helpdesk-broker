package com.iglo.chatbothelpdesk.model.dolphin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BroadcastRequest {
    private String name;
    private String channel;
    private String sendingMode;
    private String templateId;
    private List<BodyVariable> headerVariables;
    private List<BodyVariable> bodyVariables;
    private List<Content> content;
    private List<Map<String, String>> whatsappRecipient;

    @Data
    @AllArgsConstructor
    public static class BodyVariable{
        // private String type;
        private String key;
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class Content{
        private String type;
        private String url;
        private String message;
        private String thumbnailUrl;
    }

}
