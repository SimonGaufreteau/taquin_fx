package com.taquin;

import javafx.util.Pair;

import java.util.Locale;
import java.util.Random;

public class Agent extends Thread {
	private final int ID;
	private Puzzle puzzle;
	private final static int SLEEP_TIME = 500;
	private final static boolean IS_SEQUENTIAL = false;
	private MoveStrategy strategy;
	private Random random;
	private long wait_time;

	private String name;

	public Agent(int i, Puzzle puzzle) {
		this(i,puzzle,"basicmovestrategy");
	}

	public Agent(int i, Puzzle puzzle, String strategyName){
		super();
		this.ID = i;
		this.puzzle = puzzle;
		this.setStrategy(strategyName);
		this.random = new Random();
		this.wait_time = random.nextInt(puzzle.getNbAgent()*100);
		this.name = "old";
	}

	public Agent(int id, Puzzle puzzle, MoveStrategy strategy) {
		this(id,puzzle);
		this.strategy=strategy;
	}

	public void setAgentName(String name){
		this.name = name;
	}


	public static Agent getNewCopy(Agent agent) {
		return new Agent(agent.ID,agent.puzzle,agent.strategy.getName());
	}

	@Override
	public void run() {
		System.out.println("Agent "+this+ " started");
		try {
			if(IS_SEQUENTIAL)
				Thread.sleep((long) SLEEP_TIME *ID);
			else
				Thread.sleep(wait_time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(!puzzle.isFinished()) {
			// Check if the Agent is on the right position

			// First check to move if not at the end or the strategy uses a mixed priority policy
			// (which may take priority even if the agent is at its final position)
			if(!isAtDestination() || strategy.hasMixedPriority()){
				move();
			}

			// Second check to stop if at the right pos
			// (if we don't do this and just an "else" the puzzle sometimes finishes during the thread sleep and
			// we don't get to see if the agent really finished)
			if(isAtDestination() && !strategy.hasMixedPriority()){
				System.out.println("Agent "+ID+" has arrived");
				break;
			}
			try {
				long sleep_time_adj = IS_SEQUENTIAL? (long) SLEEP_TIME *puzzle.getNbAgent():SLEEP_TIME;
				Thread.sleep(sleep_time_adj);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Agent "+this+ " finished");
	}

	public boolean isAtDestination() {
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
	 * @throws Exception if 1 <= n <= 2 is not verified
	 */
	protected Direction[] getBestDirections(int n) throws Exception {
		if(n!=2 && n!=1)
			throw new Exception("Wrong size of direction list. Valid values are 1 and 2.");
		Direction[] directions = new Direction[n];

		int distX = getDistance(Axes.X);
		int distY = getDistance(Axes.Y);

		if(Math.abs(distX)>Math.abs(distY)){
			directions[0] = distX>0?Direction.RIGHT:Direction.LEFT;
			if(n>1){
				if(distY==0){
					Random random = new Random();
					directions[1] = random.nextInt(2)==1?Direction.TOP:Direction.BOTTOM;
				}
				else
					directions[1] = distY>0?Direction.BOTTOM:Direction.TOP;
			}

		}
		else {
			directions[0] = distY>0?Direction.BOTTOM:Direction.TOP;
			if(n>1) {
				if (distX == 0) {
					Random random = new Random();
					directions[1] = random.nextInt(2) == 1 ? Direction.RIGHT : Direction.LEFT;
				} else
					directions[1] = distX > 0 ? Direction.RIGHT : Direction.LEFT;
			}
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

	/**
	 * Sets the strategy according to the given String. Defaults to {@link BasicMoveStrategy}.
	 * @see MoveStrategy
	 */
	public void setStrategy(String strategyName) {
		this.strategy = switch (strategyName.toLowerCase(Locale.ROOT)){
			case "messagestrategy" -> new MessageStrategy(this,puzzle);
			default -> new BasicMoveStrategy(this,puzzle);
		};
	}

	@Override
	public String toString() {
		return "Agent{" + name +
				" ID=" + ID +
				'}';
	}

	private enum Axes {X,Y}
}
