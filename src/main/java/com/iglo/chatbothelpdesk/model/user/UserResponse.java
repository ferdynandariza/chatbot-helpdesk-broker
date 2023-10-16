package com.iglo.chatbothelpdesk.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private String name;

    private Long companyId;

    private String companyName;
}
