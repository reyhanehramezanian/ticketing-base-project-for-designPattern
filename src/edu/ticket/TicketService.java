package edu.ticket;

public class TicketService {
    
    public void handle(String channel, String type) {
        Ticket ticket = TicketFactory.createTicket(channel, type);
        
        ticket.process();
    }
}
