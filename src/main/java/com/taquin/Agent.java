package com.taquin;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observable;

public class Agent extends Thread {

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

	public void move() {
		Direction[] directions = getBestDirections();
		Pair<Integer,Integer> currentPos = puzzle.getAgentPos().get(this);
		Agent[][] currentGrid = puzzle.getCurrentGrid();
		int x = currentPos.getKey();
		int y = currentPos.getValue();
		boolean moved = false;

		moved = switch(directions[0]) {
			case TOP -> y - 1 >= 0 && currentGrid[y - 1][x] == null && puzzle.moveAgent(this, Direction.TOP);

			case RIGHT -> x + 1 < currentGrid[0].length && currentGrid[y][x + 1] == null && puzzle.moveAgent(this, Direction.RIGHT);

			case BOTTOM -> y + 1 < currentGrid.length && currentGrid[y + 1][x] == null && puzzle.moveAgent(this, Direction.BOTTOM);

			case LEFT -> x - 1 >= 0 && currentGrid[y][x - 1] == null && puzzle.moveAgent(this, Direction.LEFT);
		};

		if(!moved){
			switch(directions[1]) {
				case TOP -> {
					if (y - 1 >= 0 && currentGrid[y - 1][x] == null) {
						puzzle.moveAgent(this, Direction.TOP);
					}
				}

				case RIGHT -> {
					if (x + 1 < currentGrid[0].length && currentGrid[y][x + 1] == null) {
						puzzle.moveAgent(this, Direction.RIGHT);
					}
				}

				case BOTTOM -> {
					if (y + 1 < currentGrid.length && currentGrid[y + 1][x] == null) {
						puzzle.moveAgent(this, Direction.BOTTOM);
					}
				}

				case LEFT -> {
					if (x - 1 >= 0 && currentGrid[y][x - 1] == null) {
						puzzle.moveAgent(this, Direction.LEFT);
					}
				}
			};
		}
	}

	/**
	 * @return An array of size 2 => The best directions to take on X and Y (ex: [TOP, LEFT] )
	 */
	private Direction[] getBestDirections() {
		Direction[] directions = new Direction[2];

		int distX = getDistance(Axes.X);
		int distY = getDistance(Axes.Y);

		if(Math.abs(distX)>Math.abs(distY)){
			directions[0] = distX>0?Direction.RIGHT:Direction.LEFT;
			directions[1] = distY>0?Direction.TOP:Direction.BOTTOM;
		}
		else {
			directions[0] = distY>0?Direction.TOP:Direction.BOTTOM;
			directions[1] = distX>0?Direction.RIGHT:Direction.LEFT;
		}

		return directions;
	}

	private int getDistance(Axes x) {
		Pair<Integer,Integer> currentPos = puzzle.getAgentPos().get(this);
		Pair<Integer,Integer> destinationPos = puzzle.getAgentPos().get(this);
		if(x.equals(Axes.X)){
			return destinationPos.getKey()-currentPos.getKey();
		}
		return destinationPos.getValue()-currentPos.getValue();

	}


	private enum Axes {X,Y}
}
