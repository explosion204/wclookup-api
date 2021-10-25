package com.explosion204.wclookup.service.dto;

import com.explosion204.wclookup.model.entity.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TicketDto extends IdentifiableDto {
    @Size(min = 1, max = 140)
    private String subject;

    @Size(min = 1, max = 1024)
    private String text;

    @Size(min = 3, max = 100)
    @Email
    private String email;

    private LocalDateTime creationTime;
    private boolean isResolved;

    public Ticket toTicket() {
        Ticket ticket = new Ticket();

        ticket.setId(id);
        ticket.setSubject(subject);
        ticket.setText(text);
        ticket.setEmail(email);
        ticket.setResolved(isResolved);

        return ticket;
    }

    public static TicketDto fromTicket(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();

        ticketDto.subject = ticket.getSubject();
        ticketDto.text = ticket.getText();
        ticketDto.email = ticket.getEmail();
        ticketDto.isResolved = ticket.isResolved();

        return ticketDto;
    }
}
