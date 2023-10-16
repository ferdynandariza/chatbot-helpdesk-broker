package com.iglo.chatbothelpdesk.dao;

import com.iglo.chatbothelpdesk.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord, String> {

    Optional<FileRecord> findFirstById(String id);

}
