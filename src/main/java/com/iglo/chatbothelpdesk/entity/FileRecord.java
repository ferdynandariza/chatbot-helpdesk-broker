package com.iglo.chatbothelpdesk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class FileRecord {

    @Id
    private String id;

    private String fileName;

    private String extension;

    @Lob
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "ticketId", referencedColumnName = "id")
    private Ticket ticket;

}
