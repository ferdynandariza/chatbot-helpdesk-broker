package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import com.iglo.chatbothelpdesk.model.file.FileResponse;
import com.iglo.chatbothelpdesk.model.file.SendFileRequest;
import com.iglo.chatbothelpdesk.model.file.StoreFileResponse;
import com.iglo.chatbothelpdesk.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreFileResponse> storeFile(@RequestBody SendFileRequest request){
        StoreFileResponse response = fileService.storeFile(request);
        return WebResponse.<StoreFileResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/{fileId}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<StreamingResponseBody> storeFile(@PathVariable Long fileId){
        FileResponse fileResponse = fileService.retrieveFile(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=%s", fileResponse.getFileName()))
                .body(outputStream -> {
                    try {
                    IOUtils.copy(new ByteArrayInputStream(fileResponse.getFileData()), outputStream);
                    } catch (IOException e){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve file");
                    }
                }
                );

    }

}
