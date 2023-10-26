package com.iglo.chatbothelpdesk.model.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class StoreFileResponse {

    private String fileId;

    private String fileName;

    private String extension;

    private String path;
}
