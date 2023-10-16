package com.iglo.chatbothelpdesk.service.implementation;

import com.iglo.chatbothelpdesk.dao.FileRecordRepository;
import com.iglo.chatbothelpdesk.dao.TicketRepository;
import com.iglo.chatbothelpdesk.dao.UserRepository;
import com.iglo.chatbothelpdesk.entity.FileRecord;
import com.iglo.chatbothelpdesk.entity.Ticket;
import com.iglo.chatbothelpdesk.entity.User;
import com.iglo.chatbothelpdesk.model.ticket.TicketRequest;
import com.iglo.chatbothelpdesk.model.ticket.TicketResponse;
import com.iglo.chatbothelpdesk.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final FileRecordRepository fileRecordRepository;

    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, FileRecordRepository fileRecordRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.fileRecordRepository = fileRecordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TicketResponse createTicket(TicketRequest request) {
        Ticket ticket = saveTicket(request);

        setAttachmentsTicket(request.getAttachmentsId(), ticket);

        return new TicketResponse(ticket.getNumber(), ticket.getRequestor().getId());
    }

    private String generateTicketNumber() {
        UUID uuid = UUID.randomUUID();
        String uniqueStr = uuid.toString().substring(0, 4);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return String.format("TICKET-%s-%s", now.format(formatter), uniqueStr);
    }

    private Ticket saveTicket(TicketRequest request) {
        Ticket ticket = new Ticket();
        User requestor = userRepository.findById(request.getRequestorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exists"));

        ticket.setRequestor(requestor);
        ticket.setNumber(generateTicketNumber());
        ticket.setDescription(request.getDescription());
        ticket.setCreatedAt(System.currentTimeMillis());

        return ticketRepository.save(ticket);
    }

    private void setAttachmentsTicket(List<String> attachmentsId, Ticket ticket) {
        attachmentsId.forEach(
                id -> {
                    FileRecord file = fileRecordRepository.findFirstById(id).orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not exists")
                    );
                    file.setTicket(ticket);
                    fileRecordRepository.save(file);
                });
    }


}
