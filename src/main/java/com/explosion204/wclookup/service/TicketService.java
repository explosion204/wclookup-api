package com.explosion204.wclookup.service;

import com.explosion204.wclookup.exception.EntityNotFoundException;
import com.explosion204.wclookup.model.entity.Ticket;
import com.explosion204.wclookup.model.repository.TicketRepository;
import com.explosion204.wclookup.service.dto.identifiable.TicketDto;
import com.explosion204.wclookup.service.pagination.PageContext;
import com.explosion204.wclookup.service.pagination.PaginationModel;
import com.explosion204.wclookup.service.validation.annotation.ValidateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public PaginationModel<TicketDto> findAll(PageContext pageContext) {
        PageRequest pageRequest = pageContext.toPageRequest();
        Page<TicketDto> page = ticketRepository.findAll(pageRequest)
                .map(TicketDto::fromTicket);
        return PaginationModel.fromPage(page);
    }

    public TicketDto findById(long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Ticket.class));
        return TicketDto.fromTicket(ticket);
    }

    @ValidateDto
    public TicketDto create(TicketDto ticketDto) {
        Ticket ticket = ticketDto.toTicket();
        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketDto.fromTicket(savedTicket);
    }

    public void delete(long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Ticket.class));
        ticketRepository.delete(ticket);
    }
}
