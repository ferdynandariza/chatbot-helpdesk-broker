package com.iglo.chatbothelpdesk.service;

import com.iglo.chatbothelpdesk.model.ticket.TicketRequest;
import com.iglo.chatbothelpdesk.model.ticket.TicketResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {

    TicketResponse createTicket(TicketRequest request);

    TicketResponse getTicket(Long ticketId);

    Page<TicketResponse> searchTickets(String ticketNumber, Integer page, Integer size);
}
