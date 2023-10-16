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
        String generatedFileName = generateFileName();

        asyncStoreFile(request, generatedFileName);

        return new StoreFileResponse(request.getFileName(), generatedFileName, request.getExtension());
    }

    private String generateFileName() {
        UUID uuid = UUID.randomUUID();
        String uniqueStr = uuid.toString().substring(0, 8);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return String.format("F-%s-%s", now.format(formatter), uniqueStr.toUpperCase());
    }
    @Async
    protected void asyncStoreFile(SendFileRequest request, String generatedFileName) {
        URL fileUrl = getUrl(request);
        byte[] byteArray = getByteArray(fileUrl);

        FileRecord file = new FileRecord();
        file.setFileName(request.getFileName());
        file.setFileData(byteArray);
        file.setGeneratedName(generatedFileName);
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
        if (byteArray.length == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        return byteArray;
        } catch (IOException e){
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed retrieving file");
        }
    }

    @Override
    public FileResponse retrieveFile(Long fileId) {
        FileRecord fileRecord = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not exists")
                );
        return new FileResponse(
                fileId,
                fileRecord.getFileName() + "." + fileRecord.getExtension(),
                fileRecord.getFileData());
    }

}
