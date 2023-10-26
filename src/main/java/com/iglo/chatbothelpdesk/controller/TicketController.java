package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.PagingResponse;
import com.iglo.chatbothelpdesk.model.WebResponse;
import com.iglo.chatbothelpdesk.model.ticket.TicketRequest;
import com.iglo.chatbothelpdesk.model.ticket.TicketResponse;
import com.iglo.chatbothelpdesk.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TicketResponse>> search(
            @RequestParam(name = "number", required = false, defaultValue = "") String number,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ){
        Page<TicketResponse> responses = ticketService.searchTickets(number, page, size);
        List<TicketResponse> ticketResponses = responses.stream().toList();

        return WebResponse.<List<TicketResponse>>builder()
                .data(ticketResponses)
                .paging(PagingResponse.builder()
                        .currentPage(page).size(size).totalPages(responses.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping(
            path = "/{ticketId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TicketResponse> search(
            @PathVariable(name = "ticketId") Long ticketId
    ){
        TicketResponse response = ticketService.getTicket(ticketId);
        return WebResponse.<TicketResponse>builder().data(response).build();
    }


}
