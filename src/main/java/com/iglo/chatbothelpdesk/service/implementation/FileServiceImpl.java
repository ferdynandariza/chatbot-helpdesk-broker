package com.iglo.chatbothelpdesk.service.implementation;

import com.iglo.chatbothelpdesk.dao.FileRecordRepository;
import com.iglo.chatbothelpdesk.entity.FileRecord;
import com.iglo.chatbothelpdesk.model.file.FileResponse;
import com.iglo.chatbothelpdesk.model.file.SendFileRequest;
import com.iglo.chatbothelpdesk.model.file.StoreFileResponse;
import com.iglo.chatbothelpdesk.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRecordRepository fileRecordRepository;

    public FileServiceImpl(FileRecordRepository fileRecordRepository) {
        this.fileRecordRepository = fileRecordRepository;
    }


    @Transactional
    @Override
    public StoreFileResponse storeFile(SendFileRequest request) {
        String id = UUID.randomUUID().toString();
        String generatedFileName = generateFileName(id);

        asyncStoreFile(id, request, generatedFileName);
        /*
        // Thread safe, no limitation of file size
        // the drawbacks are file may be not stored yet while accessed
        Runnable runnable = () -> {
            asyncStoreFile(id, request, generatedFileName);
        };
        Thread thread = new Thread(runnable);
        thread.start();
        */

        return new StoreFileResponse(id, generatedFileName, request.getExtension());
    }

    protected String generateFileName(String id) {
        String uniqueStr = id.substring(0, 4);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return String.format("F_%s_%s", now.format(formatter), uniqueStr);
    }
    @Async
    protected void asyncStoreFile(String id, SendFileRequest request, String generatedFileName) {
        URL fileUrl = getUrl(request);
        log.info("Retrieving bytes");
        byte[] byteArray = getByteArray(fileUrl);
        FileRecord file = new FileRecord();
        file.setId(id);
        file.setFileName(generatedFileName);
        file.setFileData(byteArray);
        file.setExtension(request.getExtension());
        fileRecordRepository.save(file);
    }

    protected URL getUrl(SendFileRequest request) {
        try {
            return new URL(request.getUrlFile()
                    .replace(" ", "%20"));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL Invalid");
        }
    }

    protected byte[] getByteArray(URL url) {
        try (InputStream in = url.openStream()) {
        byte[] byteArray = IOUtils.toByteArray(in);
        log.info("File size: {} kb", (double) byteArray.length / 1024);
        if (byteArray.length == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File invalid");
        return byteArray;
        } catch (IOException e){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    @Transactional
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
