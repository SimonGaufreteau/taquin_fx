package com.taquin;

public class MessageStrategy extends MoveStrategy{
    private boolean sleepNext;
    private final String name = "messagestrategy";

    public MessageStrategy(Agent agent, Puzzle puzzle) {
        super(agent, puzzle);
        // The agent may move even if it's at his final position due to the mailbox
        mixedPriority = true;
        sleepNext = false;
    }

    @Override
    boolean move() {
        // Sleep if required
        if(sleepNext){
            sleepNext = false;
            return false;
        }

        // Check mailbox
        MailBox mailBox = puzzle.getMailBox();
        Message messageReceived;
        try {
             messageReceived = mailBox.getMessage(agent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Direction[] perpendicularDirections;
        Direction receivedDirection;
        // If we have a message, try move from our position and wait the next step
        if(messageReceived!=null){
            //System.out.printf("Agent %s received %s, trying to process it%n",agent,messageReceived);
            receivedDirection = messageReceived.getDirection();
            // First try to move in the perpendicular directions
            perpendicularDirections = DirectionUtils.getPerpendicularDirections(receivedDirection);
            for(Direction direction : perpendicularDirections){
                try {
                    if(puzzle.getAgentInDirection(agent,direction)==null){
                        sleepNext = true;
                        return puzzle.moveAgent(agent,direction);
                    }
                } catch (Exception ignored){

                }
            }

            // if we couldn't move before, try to move in the direction given by the sender
            // This may help in situation where the agent can only move "backwards" and make space for the sender
            try{
                if(puzzle.getAgentInDirection(agent,receivedDirection)==null){
                    sleepNext = true;
                    return puzzle.moveAgent(agent,receivedDirection);
                }
            }catch (Exception ignored) {}
        }
        // Don't move when the agent is at destination and the mailbox is empty
        else if(agent.isAtDestination())
            return false;

        Agent recipient;
        boolean moved = false;
        Direction[] bestDirections;
        try {
           bestDirections = agent.getBestDirections(2);
        }catch (Exception e){
            // This should never happen in theory
            e.printStackTrace();
            return false;
        }

        try{
            recipient = puzzle.getAgentInDirection(agent,bestDirections[0]);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        // Try to move in the obvious directions first
        BasicMoveStrategy basicMoveStrategy = new BasicMoveStrategy(agent,puzzle);
        moved = basicMoveStrategy.move();

        // If we couldn't move, something is blocking us. We send a moving message
        if(!moved){
            Message messageSent = new Message(MessageType.MOVE,agent,recipient,bestDirections[0]);
            try {
                mailBox.sendMessage(recipient,messageSent);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return moved;
    }
}
