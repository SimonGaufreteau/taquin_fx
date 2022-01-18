package com.taquin;

import javafx.util.Pair;

public class BasicMoveStrategy extends MoveStrategy{
    private int sizeDirection;
    private final String name = "basicmovestrategy";

    public BasicMoveStrategy(Agent agent, Puzzle puzzle, int sizeDirection) {
        super(agent, puzzle);
        this.sizeDirection = sizeDirection;
    }

    public BasicMoveStrategy(Agent agent, Puzzle puzzle){
        this(agent, puzzle,2);
    }

    @Override
    boolean move() {
        Direction[] directions = new Direction[0];
        try {
            directions = agent.getBestDirections(sizeDirection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean moved = false;

        for (int i = 0; i < directions.length && !moved; i++) {
            Direction direction = directions[i];
            try{
                Agent agentDirect = puzzle.getAgentInDirection(agent,direction);
                moved = agentDirect==null && puzzle.moveAgent(agent,direction);
                // If there is an agent, we just move on
            }catch (Exception e){
                // If the next position in this direction is out of grid, we just continue to the next direction
                // e.printStackTrace();
            }
        }
        return moved;
    }
}
