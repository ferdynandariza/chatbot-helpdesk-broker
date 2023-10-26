package com.iglo.chatbothelpdesk.dao;

import com.iglo.chatbothelpdesk.entity.FileRecord;
import com.iglo.chatbothelpdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord, String> {

    Optional<FileRecord> findFirstById(String id);

    Optional<FileRecord> findFirstByTicketAndFileNameAndExtension(Ticket ticket, String fileName, String extension);

    List<FileRecord> findAllByTicket(Ticket ticket);

}
