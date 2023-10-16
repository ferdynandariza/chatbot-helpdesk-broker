package com.iglo.chatbothelpdesk.service.implementation;

import com.iglo.chatbothelpdesk.dao.FileRecordRepository;
import com.iglo.chatbothelpdesk.entity.FileRecord;
import com.iglo.chatbothelpdesk.model.file.FileResponse;
import com.iglo.chatbothelpdesk.model.file.SendFileRequest;
import com.iglo.chatbothelpdesk.model.file.StoreFileResponse;
import com.iglo.chatbothelpdesk.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileRecordRepository fileRecordRepository;

    public FileServiceImpl(FileRecordRepository fileRecordRepository) {
        this.fileRecordRepository = fileRecordRepository;
    }


    @Override
    public StoreFileResponse storeFile(SendFileRequest request) {
        String id = UUID.randomUUID().toString();
        String generatedFileName = generateFileName(id, request.getExtension());

        asyncStoreFile(id, request, generatedFileName);

        return new StoreFileResponse(id, generatedFileName, request.getExtension());
    }

    private String generateFileName(String id, String extension) {
        String uniqueStr = id.substring(0, 4);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return String.format("F_%s_%s.%s", now.format(formatter), uniqueStr, extension);
    }
    @Async
    protected void asyncStoreFile(String id, SendFileRequest request, String generatedFileName) {
        URL fileUrl = getUrl(request);
        byte[] byteArray = getByteArray(fileUrl);

        FileRecord file = new FileRecord();
        file.setId(id);
        file.setFileName(generatedFileName);
        file.setFileData(byteArray);
        file.setExtension(request.getExtension());
        fileRecordRepository.save(file);
    }

    private URL getUrl(SendFileRequest request) {
        try { return new URL(request.getUrlFile());
        } catch (MalformedURLException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL Invalid");
        }
    }

    private byte[] getByteArray(URL url) {
        try (InputStream in = url.openStream()) {
        byte[] byteArray = IOUtils.toByteArray(in);
        if (byteArray.length == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File invalidd");
        return byteArray;
        } catch (IOException e){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    @Override
    public FileResponse retrieveFile(String  fileId) {
        FileRecord fileRecord = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not exists")
                );
        return new FileResponse(
                fileId,
                fileRecord.getFileName() + "." + fileRecord.getExtension(),
                fileRecord.getFileData());
    }

}
