package com.iglo.chatbothelpdesk.service;

import com.iglo.chatbothelpdesk.model.ticket.TicketRequest;
import com.iglo.chatbothelpdesk.model.ticket.TicketResponse;

public interface TicketService {

    TicketResponse createTicket(TicketRequest request);
}
