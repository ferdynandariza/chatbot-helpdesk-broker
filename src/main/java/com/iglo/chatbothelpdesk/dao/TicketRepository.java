package com.iglo.chatbothelpdesk.dao;

import com.iglo.chatbothelpdesk.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAllByNumberContaining(Optional<String> number, Pageable pageable);
}
