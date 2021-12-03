package com.taquin;

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
