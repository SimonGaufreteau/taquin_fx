package com.taquin;

import java.util.LinkedList;

public abstract class MoveStrategy {
    protected Agent agent;
    protected Puzzle puzzle;
    protected boolean mixedPriority = false;
    protected String name = "movestrategy";
    protected LinkedList<Direction> patternBuffer;
    protected final static int PATTERN_BUFFER_SIZE = 10;


    public MoveStrategy(Agent agent, Puzzle puzzle){
        this.agent = agent;
        this.puzzle = puzzle;
        this.patternBuffer = new LinkedList<>();

    }

    abstract Direction move();

    /**
     * @return a boolean indicating if the strategy may do something when the agent is at its destination
     */
    public boolean hasMixedPriority() {
        return mixedPriority;
    }

    public String getName() {
        return name;
    }

    public void addToBuffer(Direction direction){
        if(patternBuffer.size()==PATTERN_BUFFER_SIZE){
            patternBuffer.poll();
        }
        patternBuffer.add(direction);
    }
}
