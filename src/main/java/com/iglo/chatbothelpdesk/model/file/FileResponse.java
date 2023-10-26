package com.iglo.chatbothelpdesk.model.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private String fileId;

    private String fileName;

    private byte[] fileData;

    private StreamingResponseBody streamingData;
}
