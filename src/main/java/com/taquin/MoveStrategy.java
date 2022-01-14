package com.taquin;

public abstract class MoveStrategy {
    protected Agent agent;
    protected Puzzle puzzle;

    public MoveStrategy(Agent agent, Puzzle puzzle){
        this.agent = agent;
        this.puzzle = puzzle;
    }

    abstract boolean move();

}
