package edu.ticket;

interface TicketState {
    void handle(Ticket context);
}

class NewState implements TicketState {
    @Override
    public void handle(Ticket context) {
        System.out.println("Ticket created");
        context.receiveRequest();  
        context.setState(new AssignedState());
    }
}

class AssignedState implements TicketState {
    @Override
    public void handle(Ticket context) {
        context.handleAssignment(); 
        context.setState(new InProgressState());
    }
}

class InProgressState implements TicketState {
    @Override
    public void handle(Ticket context) {
        System.out.println("Working on ticket");
        context.getTypeStrategy().respond(); 
        context.setState(new ResolvedState());
    }
}

class ResolvedState implements TicketState {
    @Override
    public void handle(Ticket context) {
        System.out.println("Ticket resolved");
        context.setState(new ClosedState());
    }
}

class ClosedState implements TicketState {
    @Override
    public void handle(Ticket context) {
        System.out.println("Ticket closed");
    }
}

interface ChannelStrategy {
    void receive();
}

class WebChannel implements ChannelStrategy {
    @Override
    public void receive() { System.out.println("Received from web"); }
}

class EmailChannel implements ChannelStrategy {
    @Override
    public void receive() { System.out.println("Received from email"); }
}

interface TicketTypeStrategy {
    void assign();
    void respond();
}

class BugStrategy implements TicketTypeStrategy {
    @Override
    public void assign() { System.out.println("Assigned to engineering"); }
    @Override
    public void respond() { System.out.println("Sending bug response"); }
}

class SupportStrategy implements TicketTypeStrategy {
    @Override
    public void assign() { System.out.println("Assigned to support"); }
    @Override
    public void respond() { System.out.println("Sending generic response"); }
}

class TicketFactory {
    public static Ticket createTicket(String channelStr, String typeStr) {
        ChannelStrategy channel = channelStr.equalsIgnoreCase("WEB") ? new WebChannel() : new EmailChannel();
        TicketTypeStrategy type = typeStr.equalsIgnoreCase("BUG") ? new BugStrategy() : new SupportStrategy();
        return new Ticket(channel, type);
    }
}

public class Ticket {
    private TicketState currentState;
    private ChannelStrategy channel;
    private TicketTypeStrategy type;

    public Ticket(ChannelStrategy channel, TicketTypeStrategy type) {
        this.channel = channel;
        this.type = type;
        this.currentState = new NewState(); 
    }

    public void setState(TicketState state) { this.currentState = state; }
    public TicketTypeStrategy getTypeStrategy() { return type; }
    
    public void receiveRequest() { channel.receive(); }
    public void handleAssignment() { type.assign(); }

    public void process() {
        while (!(currentState instanceof ClosedState)) {
            currentState.handle(this);
        }
        currentState.handle(this);  
        System.out.println("Logging ticket handling");
    }
}