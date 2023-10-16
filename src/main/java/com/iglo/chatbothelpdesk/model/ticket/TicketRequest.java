package com.iglo.chatbothelpdesk.model.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {

    private Long requestorId;

    private String description;

    private List<String> attachmentsId;

}
