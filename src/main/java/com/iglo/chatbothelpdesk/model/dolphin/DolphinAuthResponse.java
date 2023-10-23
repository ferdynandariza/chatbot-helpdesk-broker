package com.iglo.chatbothelpdesk.model.dolphin;

import lombok.Data;

@Data
public class DolphinAuthResponse {
    private String token;
    private String expireAt;
}
