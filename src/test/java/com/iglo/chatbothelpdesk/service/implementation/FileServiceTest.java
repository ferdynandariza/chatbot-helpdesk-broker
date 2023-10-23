package com.iglo.chatbothelpdesk.service.implementation;

import com.iglo.chatbothelpdesk.dao.FileRecordRepository;
import com.iglo.chatbothelpdesk.model.file.SendFileRequest;
import com.iglo.chatbothelpdesk.model.file.StoreFileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    FileRecordRepository fileRecordRepository;

    @Autowired
    FileServiceImpl fileService;

    @Test
    void noDuplicateNamesOrId(){
        SendFileRequest request = new SendFileRequest(
                "https://dittel.kominfo.go.id/attachments/blank.pdf",
                ".pdf");

        StoreFileResponse response1 = fileService.storeFile(request);
        StoreFileResponse response2 = fileService.storeFile(request);

        assertEquals(request.getExtension(), response1.getExtension());
        assertEquals(request.getExtension(), response2.getExtension());
        assertNotEquals(response1.getFileId(), response2.getFileId());
        assertNotEquals(response1.getFileName(), response2.getFileName());
    }

    @Test
    void noIdenticalName(){
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String id = UUID.randomUUID().toString();
            String name = fileService.generateFileName(id);
            names.add(name);
            System.out.println(name);
        }

        for (int i = 0; i <100 ; i++) {
            List<String> temp = new ArrayList<>();
            temp.addAll(names);
            temp.remove(i);
            assertFalse(temp.contains(names.get(i)));
        }
    }


}