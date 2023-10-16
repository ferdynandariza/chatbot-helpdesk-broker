package com.iglo.chatbothelpdesk.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private String fileId;

    private String fileName;

    private byte[] fileData;
}
