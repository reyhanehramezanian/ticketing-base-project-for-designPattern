package edu.ticket;

public class TicketService {
    
    public void handle(String channel, String type) {
        Ticket ticket = TicketFactory.createTicket(channel, type);
        
        ticket.process();
    }

     public static void main(String[] args) {
        TicketService service = new TicketService();
        
        System.out.println("--- Test 1: Web Bug ---");
        service.handle("WEB", "BUG");
        
        System.out.println("\n--- Test 2: Email Support ---");
        service.handle("EMAIL", "SUPPORT");
    }
}
