package com.iglo.chatbothelpdesk.model.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketResponse {

    private String ticketNumber;

    private Long requestorId;

}
