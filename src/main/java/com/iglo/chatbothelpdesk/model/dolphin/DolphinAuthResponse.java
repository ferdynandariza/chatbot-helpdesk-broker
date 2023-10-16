package com.iglo.chatbothelpdesk.model.dolphin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DolphinAuthResponse {
    private String token;
    private LocalDateTime expireAt;
}
