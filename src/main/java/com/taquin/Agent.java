package com.taquin;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Agent extends Thread{

	private final int ID;
	private Puzzle puzzle;

	public Agent(int i, Puzzle puzzle) {
		super();
		this.ID = i;
		this.puzzle = puzzle;
	}

	@Override
	public void run() {
		while(!puzzle.isFinished()) {
			System.out.println("Agent "+ID+" is doing something");
			// Check if the Agent is on the right position
			if(!puzzle.getAgentDestination().get(this).equals(puzzle.getAgentPos().get(this))){
				// If not, try to move in the best direction
				move();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public int getID() {
		return this.ID;
	}
}
