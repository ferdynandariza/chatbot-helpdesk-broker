package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import com.iglo.chatbothelpdesk.model.file.FileResponse;
import com.iglo.chatbothelpdesk.model.file.SendFileRequest;
import com.iglo.chatbothelpdesk.model.file.StoreFileResponse;
import com.iglo.chatbothelpdesk.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(
            path = "/files",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreFileResponse> storeFile(@RequestBody SendFileRequest request) {
        StoreFileResponse response = fileService.storeFile(request);
        return WebResponse.<StoreFileResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/files/{fileId}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable String fileId) {
        FileResponse fileResponse = fileService.retrieveFile(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", fileResponse.getFileName()))
                .body(fileResponse.getStreamingData());
    }

    @GetMapping(
            path = "/tickets/{ticketId}/files",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<StoreFileResponse>> getFilesUrl(@PathVariable Long ticketId) {
        List<StoreFileResponse> responses = fileService.getAllFiles(ticketId);
        return WebResponse.<List<StoreFileResponse>>builder().data(responses).build();
    }

    @GetMapping(
            path = "/tickets/{ticketId}/files/{fileName}.{extension}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable Long ticketId,
                                                              @PathVariable String fileName,
                                                              @PathVariable String extension) {

        FileResponse fileResponse = fileService.retrieveFile(ticketId, fileName, extension);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", fileResponse.getFileName()))
                .body(fileResponse.getStreamingData());
    }

}
