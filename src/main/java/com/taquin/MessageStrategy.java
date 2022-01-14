package com.taquin;

import javafx.util.Pair;

public class MessageStrategy extends MoveStrategy{
    public MessageStrategy(Agent agent, Puzzle puzzle) {
        super(agent, puzzle);
    }

    @Override
    boolean move() {
        boolean moved = false;
        Pair<Integer,Integer> currentPos = puzzle.getAgentPos().get(agent);
        Agent recipient;
        Direction bestDirection;
        try {
            bestDirection = agent.getBestDirections(1)[0];
            recipient = puzzle.getAgentInDirection(agent,bestDirection);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        // Try to move in the obvious direction first
        if(recipient==null)
            moved = puzzle.moveAgent(agent,bestDirection);

        // If we couldn't move, something is blocking us. We send a moving message
        if(!moved){
            Message message = new Message(MessageType.MOVE,agent,recipient);
            try {
                MailBox mailBox = puzzle.getMailBox();
                mailBox.sendMessage(recipient,message);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return moved;
    }
}
