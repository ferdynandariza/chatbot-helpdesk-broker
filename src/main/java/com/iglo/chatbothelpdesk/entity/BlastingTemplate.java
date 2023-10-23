package com.iglo.chatbothelpdesk.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class BlastingTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private String channel;

    @Column
    private String templateId;

    @Column
    private String phone;

    @Column(columnDefinition = "text")
    private String fields;

    @Column
    private Integer sendCount;

}
