package com.taquin;

public abstract class MoveStrategy {
    protected Agent agent;
    protected Puzzle puzzle;
    protected boolean mixedPriority = false;
    protected String name = "movestrategy";

    public MoveStrategy(Agent agent, Puzzle puzzle){
        this.agent = agent;
        this.puzzle = puzzle;
    }

    abstract boolean move();

    /**
     * @return a boolean indicating if the strategy may do something when the agent is at its destination
     */
    public boolean hasMixedPriority() {
        return mixedPriority;
    }

    public String getName() {
        return name;
    }
}
