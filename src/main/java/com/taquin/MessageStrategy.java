package com.taquin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MessageStrategy extends MoveStrategy{
    private int stepsToSleep;
    private final String name = "messagestrategy";
    private final static int MIN_STEPS = 40;
    private int stepsTaken;
    private int dir_pattern;

    public MessageStrategy(Agent agent, Puzzle puzzle) {
        super(agent, puzzle);
        // The agent may move even if it's at his final position due to the mailbox
        mixedPriority = true;
        stepsToSleep = 0;
        stepsTaken = 0;
        dir_pattern = 0;
    }

    @Override
    Direction move() {
        stepsTaken++;
        // Sleep if required
        if(stepsToSleep!=0){
            stepsToSleep--;
            return null;
        }


        boolean isPattern = false;
        // Check for patterns after taking at least MIN_STEPS steps
        // If we check at the first step, we will have full null buffer and detect a pattern
        if(stepsTaken>MIN_STEPS && containsPattern()){
            // If a pattern was found in the buffer, wait a step to see if we get a message for example
            // This should resolve most repeating situations
            // Just in case, we prevent the agent from moving for this step. This resolves some "walling" situations
            isPattern = true;
            dir_pattern = (dir_pattern+1)%2;
            //return null;
        }

        // Check mailbox
        MailBox mailBox = puzzle.getMailBox();
        Message messageReceived;
        try {
             messageReceived = mailBox.getMessage(agent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Direction[] perpendicularDirections = new Direction[0];
        Direction receivedDirection;
        // If we have a message, try move from our position and wait the next step
        if(messageReceived!=null){
            //System.out.printf("Agent %s received %s, trying to process it%n",agent,messageReceived);
            receivedDirection = messageReceived.getDirection();
            // First try to move in the perpendicular directions
            List<Direction> perpendicularDirectionsList = Arrays.asList(DirectionUtils.getPerpendicularDirections(receivedDirection));
            Collections.shuffle(perpendicularDirectionsList,puzzle.getRandom());
            Direction[] directions = perpendicularDirectionsList.toArray(perpendicularDirections);
            for(Direction direction : directions){
                try {
                    if(puzzle.getAgentInDirection(agent,direction)==null){
                        stepsToSleep++;
                        if(puzzle.moveAgent(agent,direction))
                            return direction;
                        //else return null;
                    }
                } catch (Exception ignored){

                }
            }

            // if we couldn't move before, try to move in the direction given by the sender
            // This may help in situation where the agent can only move "backwards" and make space for the sender
            try{
                Agent nextAgent = puzzle.getAgentInDirection(agent,receivedDirection);
                if(nextAgent==null){
                    stepsToSleep++;
                    if(puzzle.moveAgent(agent,receivedDirection))
                        return receivedDirection;
                    else
                        nextAgent = puzzle.getAgentInDirection(agent,receivedDirection);
                }
                // If we couldn't move in any direction, relay the message to the same direction given by the first sender
                if(nextAgent!=null){
                    int waitTime = messageReceived.getStepsToWait();
                    stepsToSleep+=waitTime;
                    Message messageSent = new Message(MessageType.MOVE,agent,nextAgent,receivedDirection,stepsToSleep+1);
                    //System.out.println("Relaying the message "+messageReceived+ " to "+messageSent);
                    try {
                        mailBox.sendMessage(nextAgent, messageSent);
                        return null;
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }
            }catch (Exception ignored) {}


        }
        // Don't move when the agent is at destination and the mailbox is empty
        else if(agent.isAtDestination())
            return null;

        Agent recipient;
        Direction[] bestDirections;
        try {
           bestDirections = agent.getBestDirections(2);
        }catch (Exception e){
            // This should never happen in theory
            e.printStackTrace();
            return null;
        }

        try{
            recipient = puzzle.getAgentInDirection(agent,bestDirections[dir_pattern]);
        }catch (Exception e){
            //e.printStackTrace();
            return null;
        }
        // Try to move in the obvious directions first (if no pattern)
        Direction movedDirection = null;
        if(!isPattern){
            BasicMoveStrategy basicMoveStrategy = new BasicMoveStrategy(agent,puzzle);
            movedDirection = basicMoveStrategy.move();
        }


        // If we couldn't move, something is blocking us. We send a moving message
        if(movedDirection == null && recipient!=null){
            Message messageSent = new Message(MessageType.MOVE,agent,recipient,bestDirections[dir_pattern]);
            try {
                mailBox.sendMessage(recipient,messageSent);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return movedDirection;
    }

    /**
     * Pattern checking function. We only check for patterns of size 2 to PATTERN_BUFFER_SIZE/2
     * (an internally defined buffer size)
     * Multi pointer method used for faster detection (0(N) instead of classic 0(N^2) with bruteforce)
     * More detailed O : O(PATTERN_BUFFER_SIZE/2 *
     * @return true if a repeating pattern was found in the buffer. Else false
     */
    private boolean containsPattern(){
        /*if(Arrays.stream(patternBuffer).allMatch(Objects::isNull)){
            //System.out.println("Full null, aborting");
            return false;
        }*/
        Direction[] bufferArray = patternBuffer.toArray(new Direction[0]);

        // Check patterns of multiple size
        for(int l=2;l<PATTERN_BUFFER_SIZE/2;l++){
            int counter = 0;
            int nbStepsMax = PATTERN_BUFFER_SIZE-l-(PATTERN_BUFFER_SIZE%l);
            for(int i=0;i<nbStepsMax;i++){
                counter += bufferArray[i]==bufferArray[i+l]?1:0;
            }
            if(counter==nbStepsMax){
                if(!Arrays.stream(bufferArray).allMatch(Objects::isNull))
                    System.out.println("Pattern found : "+ Arrays.toString(bufferArray));
                return true;
            }
        }
        return false;
    }


}
