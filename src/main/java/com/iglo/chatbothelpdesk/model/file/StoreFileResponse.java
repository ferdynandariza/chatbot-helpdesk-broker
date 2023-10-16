package com.iglo.chatbothelpdesk.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreFileResponse {
//    private Long fileId;

    private String fileName;

    private String generatedName;

    private String extension;
}
