package com.taquin;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Random;

public class Agent extends Thread {
	private final int ID;
	private Puzzle puzzle;
	private final static int SLEEP_TIME = 500;
	private MoveStrategy strategy;

	public Agent(int i, Puzzle puzzle) {
		this(i,puzzle,null);
		this.setStrategy(new BasicMoveStrategy(this,puzzle));
	}

	public Agent(int i, Puzzle puzzle, MoveStrategy strategy){
		super();
		this.ID = i;
		this.puzzle = puzzle;
		this.strategy = strategy;
	}


	public static Agent getNewCopy(Agent agent) {
		return new Agent(agent.ID,agent.puzzle);
	}

	@Override
	public void run() {
		while(!puzzle.isFinished()) {
			// Check if the Agent is on the right position

			// First check to move if not at the end
			if(!isAtDestination()){
				// If not, try to move in the best direction
				move();
			}

			// Second check to stop if at the right pos
			// (if we don't do this and just an "else" the puzzle sometimes finishes during the thread sleep and
			// we don't get to see if the agent really finished)
			if(isAtDestination()){
				System.out.println("Agent "+ID+" has arrived");
				break;
			}
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(puzzle);
	}

	private boolean isAtDestination() {
		Pair<Integer, Integer> destination = puzzle.getAgentDestination().get(this);
		Pair<Integer, Integer> currentPos = puzzle.getAgentPos().get(this);
		return destination.getKey().equals(currentPos.getKey()) && destination.getValue().equals(currentPos.getValue());
	}


	public int getID() {
		return this.ID;
	}

	public void move() {
		this.strategy.move();
	}

	/**
	 * @param n size of the return array. Valid values : 1 and 2
	 * @return An array of size n => The best directions to take on X and Y (ex: [TOP, LEFT] )
	 */
	protected Direction[] getBestDirections(int n) throws Exception {
		if(n!=2 && n!=1)
			throw new Exception("Wrong size of direction list. Valid values are 1 and 2.");
		Direction[] directions = new Direction[n];

		int distX = getDistance(Axes.X);
		int distY = getDistance(Axes.Y);

		if(Math.abs(distX)>Math.abs(distY)){
			directions[0] = distX>0?Direction.RIGHT:Direction.LEFT;
			if(distY==0){
				Random random = new Random();
				directions[1] = random.nextInt(2)==1?Direction.TOP:Direction.BOTTOM;
			}
			else
				directions[1] = distY>0?Direction.BOTTOM:Direction.TOP;
		}
		else {
			directions[0] = distY>0?Direction.BOTTOM:Direction.TOP;
			if(distX==0){
				Random random = new Random();
				directions[1] = random.nextInt(2)==1?Direction.RIGHT:Direction.LEFT;
			}
			else
				directions[1] = distX>0?Direction.RIGHT:Direction.LEFT;
		}

		return directions;
	}

	private int getDistance(Axes axe) {
		Pair<Integer,Integer> currentPos = puzzle.getAgentPos().get(this);
		Pair<Integer,Integer> destinationPos = puzzle.getAgentDestination().get(this);
		if(axe.equals(Axes.X)){
			return destinationPos.getKey()-currentPos.getKey();
		}
		return destinationPos.getValue()-currentPos.getValue();
	}

	public void setStrategy(MoveStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public String toString() {
		return "Agent{" +
				"ID=" + ID +
				'}';
	}

	private enum Axes {X,Y}
}
