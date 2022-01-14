package com.taquin;

public class MessageStrategy extends MoveStrategy{
    public MessageStrategy(Agent agent, Puzzle puzzle) {
        super(agent, puzzle);
    }

    @Override
    boolean move() {
        // Try to move in the obvious direction first
        boolean moved = false;

        Pair<Integer,Integer> currentPos = puzzle.getAgentPos().get(agent);
        int x = currentPos.getKey();
        int y = currentPos.getValue();

    }
}
