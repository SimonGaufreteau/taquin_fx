package com.taquin;

public class Agent {

	private final int ID;
	private Puzzle puzzle;



	public Agent(int i, Puzzle puzzle) {
		this.ID = i;
		this.puzzle = puzzle;
	}

	public void run() {
		while(true) {
			//Listen to messages (check messages pile)

			//Move -- synchronise moving action and tile checking

			//Send messages if needed
		}
	}


	public int getID() {
		return this.ID;
	}
}
