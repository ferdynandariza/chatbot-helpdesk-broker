package com.iglo.chatbothelpdesk.service;

import com.iglo.chatbothelpdesk.model.file.FileResponse;
import com.iglo.chatbothelpdesk.model.file.SendFileRequest;
import com.iglo.chatbothelpdesk.model.file.StoreFileResponse;

import java.util.List;

public interface FileService {

    StoreFileResponse storeFile(SendFileRequest request);

    FileResponse retrieveFile(String fileId);

    List<StoreFileResponse> getAllFiles(Long ticketId);

    FileResponse retrieveFile(Long ticketId, String fileName, String extension);
}
