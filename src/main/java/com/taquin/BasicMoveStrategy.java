package com.taquin;

import javafx.util.Pair;

public class BasicMoveStrategy extends MoveStrategy{

    public BasicMoveStrategy(Agent agent, Puzzle puzzle) {
        super(agent, puzzle);
    }

    @Override
    void move() {
        Direction[] directions = agent.getBestDirections();
        Pair<Integer,Integer> currentPos = puzzle.getAgentPos().get(agent);
        Agent[][] currentGrid = puzzle.getCurrentGrid();
        int x = currentPos.getKey();
        int y = currentPos.getValue();
        boolean moved = false;

        for (int i = 0; i < directions.length && !moved; i++) {
            Direction direction = directions[i];
            moved = switch (direction) {
                case TOP -> y - 1 >= 0 && currentGrid[y - 1][x] == null && puzzle.moveAgent(agent, Direction.TOP);

                case RIGHT -> x + 1 < currentGrid[0].length && currentGrid[y][x + 1] == null && puzzle.moveAgent(agent, Direction.RIGHT);

                case BOTTOM -> y + 1 < currentGrid.length && currentGrid[y + 1][x] == null && puzzle.moveAgent(agent, Direction.BOTTOM);

                case LEFT -> x - 1 >= 0 && currentGrid[y][x - 1] == null && puzzle.moveAgent(agent, Direction.LEFT);
            };
        }
    }
}
