package com.iglo.chatbothelpdesk.service.implementation;

import com.iglo.chatbothelpdesk.dao.FileRecordRepository;
import com.iglo.chatbothelpdesk.dao.TicketRepository;
import com.iglo.chatbothelpdesk.entity.FileRecord;
import com.iglo.chatbothelpdesk.entity.Ticket;
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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRecordRepository fileRecordRepository;
    private final TicketRepository ticketRepository;
    public FileServiceImpl(FileRecordRepository fileRecordRepository, TicketRepository ticketRepository) {
        this.fileRecordRepository = fileRecordRepository;
        this.ticketRepository = ticketRepository;
    }


    @Transactional
    @Override
    public StoreFileResponse storeFile(SendFileRequest request) {
        String id = UUID.randomUUID().toString();
        String generatedFileName = generateFileName(id);
        String extension = request.getExtension().replace(".", "");
        request.setExtension(extension);

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

        return new StoreFileResponse(id, generatedFileName, extension, "/api/files/" + id);
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
                fileRecord.getFileData(),
                getStreamingData(fileRecord.getFileData()));
    }

    protected static StreamingResponseBody getStreamingData(byte[] fileData) {
        return
            outputStream -> {
            try {
            IOUtils.copy(new ByteArrayInputStream(fileData), outputStream);
            } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve file");
            }
        };
    }

    @Override
    public List<StoreFileResponse> getAllFiles(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Ticket Found With Such Id")
        );
        List<FileRecord> files = fileRecordRepository.findAllByTicket(ticket);
        return files.stream()
                .map(file -> new StoreFileResponse(
                        file.getId(),
                        file.getFileName(),
                        file.getExtension(),
                        "/api/files/" + file.getId()
                ))
                .toList();
    }

    @Override
    public FileResponse retrieveFile(Long ticketId, String fileName, String extension) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Ticket Found With Such Id")
        );
        FileRecord fileRecord = fileRecordRepository.findFirstByTicketAndFileNameAndExtension(ticket, fileName, extension)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No File Found")
                );
        return new FileResponse(
                fileRecord.getId(),
                fileRecord.getFileName() + "." + fileRecord.getExtension(),
                fileRecord.getFileData(),
                getStreamingData(fileRecord.getFileData()));
    }

}
