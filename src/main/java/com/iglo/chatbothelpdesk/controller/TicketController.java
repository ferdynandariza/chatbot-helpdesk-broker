package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import com.iglo.chatbothelpdesk.model.ticket.TicketRequest;
import com.iglo.chatbothelpdesk.model.ticket.TicketResponse;
import com.iglo.chatbothelpdesk.service.TicketService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TicketResponse> createTicket(@RequestBody TicketRequest request){
        TicketResponse response = ticketService.createTicket(request);
        return WebResponse.<TicketResponse>builder().data(response).build();
    }

}
