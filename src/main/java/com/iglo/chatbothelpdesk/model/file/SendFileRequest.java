package com.iglo.chatbothelpdesk.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendFileRequest {

    private String urlFile;

    private String fileName;

    private String extension;
}
