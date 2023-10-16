package com.iglo.chatbothelpdesk.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@ToString
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