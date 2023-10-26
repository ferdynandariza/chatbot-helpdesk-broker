package com.iglo.chatbothelpdesk.model.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketResponse {

    private Long id;

    private String ticketNumber;

    private String requestorName;

    private String description;

    private Long createdAt;

}
