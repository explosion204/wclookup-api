package com.explosion204.wclookup.model.repository;

import com.explosion204.wclookup.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
