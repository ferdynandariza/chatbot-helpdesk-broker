package com.iglo.chatbothelpdesk.service;

import com.iglo.chatbothelpdesk.model.file.FileResponse;
import com.iglo.chatbothelpdesk.model.file.SendFileRequest;
import com.iglo.chatbothelpdesk.model.file.StoreFileResponse;

public interface FileService {

    StoreFileResponse storeFile(SendFileRequest request);

    FileResponse retrieveFile(Long fileId);
}
