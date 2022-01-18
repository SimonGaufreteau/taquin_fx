package com.taquin;



import javafx.util.Pair;

import java.util.*;

/**
 * A class representing the 15puzzle
 * The grid is of size Agent[sizeY][sizeX].
 * So to access an element at position (x,y), use : currentGrid[y][x]
 */
public class Puzzle extends Observable {
	private Agent[] agentList;
	private Agent[][] destinationGrid;
	private Agent[][] currentGrid;
	private HashMap<Agent, Pair<Integer,Integer>> agentPos;
	private Map<Agent, Pair<Integer,Integer>> agentDestination;
	private int nbAgent;
	private int sizeX,sizeY;
	private Map<Agent,Integer> moveCountAgent;

	private MailBox mailBox;

	// Cache variables to enable repeatability
	private final HashMap<Agent, Pair<Integer,Integer>> baseAgentPos;



	public Puzzle(int nbAgent,int sizeX,int sizeY) {
		this(nbAgent,sizeX,sizeY,"");
	}

	public Puzzle(int nbAgent,int sizeX,int sizeY,String strategyName) {
		this.nbAgent = nbAgent;
		this.sizeX = sizeX;
		this.sizeY=sizeY;
		this.agentPos = new HashMap<>();
		this.agentDestination = new HashMap<>();
		this.moveCountAgent = new HashMap<>();

		agentList = generateAgents(strategyName);

		currentGrid = generateRandomGrid(this.agentPos);
		baseAgentPos = (HashMap<Agent, Pair<Integer, Integer>>) agentPos.clone();

		destinationGrid = generateRandomGrid(this.agentDestination);

		mailBox = new MailBox(agentList);

	}


	public void runResolution(){
		for(Agent agent : agentList){
			agent.start();
		}
	}

	/**
	 * Resets everything necessary to replay the exact same grid (grid positions, count, ...)
	 */
	public void reset(){
		this.agentPos = (HashMap<Agent, Pair<Integer, Integer>>) baseAgentPos.clone();
		this.currentGrid = new Agent[sizeY][sizeX];
		this.moveCountAgent = new HashMap<>();
		for (int i = 0; i < agentList.length; i++) {
			Agent oldAgent = agentList[i];
			Agent newAgent = Agent.getNewCopy(oldAgent);
			newAgent.setAgentName("new");
			agentList[i] = newAgent;

			// Update grid references
			Pair<Integer, Integer> oldPosition = agentPos.get(oldAgent);
			int x = oldPosition.getKey();
			int y = oldPosition.getValue();
			this.currentGrid[y][x] = newAgent;

			// Update current position references
			agentPos.remove(oldAgent);
			agentPos.put(newAgent,oldPosition);

			// Update destination position references
			agentDestination.put(newAgent,agentDestination.get(oldAgent));
			agentDestination.remove(oldAgent);

			// Update base position references
			baseAgentPos.remove(oldAgent);
			baseAgentPos.put(newAgent,oldPosition);

			// Reset move count
			moveCountAgent.put(newAgent,0);
		}
		mailBox.resetBox(agentList);
	}


	private Agent[] generateAgents() {
		return generateAgents("");
	}

	/**
	 * Generates the agent with the given strategy name. If an empty String is given, the default Agent constructor
	 * will be used.
	 * @return the agent list generated
	 * @see Agent
	 */
	private Agent[] generateAgents(String strategyName) {
		System.out.println("Generating agents with the following strategy : "+
				(strategyName.equals("")?"Default":strategyName));

		Agent[] tempList = new Agent[nbAgent];
		for(int i=0;i<nbAgent;i++){
			Agent tempAgent;
			if(strategyName.equals(""))
				tempAgent = new Agent(i,this);
			else
				tempAgent = new Agent(i,this, strategyName);
			tempList[i] = tempAgent;
			this.moveCountAgent.put(tempAgent,0);
		}
		return tempList;
	}

	public Agent[][] generateRandomGrid(Map<Agent,Pair<Integer,Integer>> posMap){
		Agent[][] tempGrid = new Agent[sizeY][sizeX];
		Random random = new Random();
		int agentIndex = 0;
		while(agentIndex<nbAgent){
			int tempX = random.nextInt(sizeX);
			int tempY = random.nextInt(sizeY);
			if(tempGrid[tempY][tempX] == null){
				tempGrid[tempY][tempX] = agentList[agentIndex];
				posMap.put(agentList[agentIndex],new Pair<>(tempX,tempY));
				agentIndex++;
			}
		}
		return tempGrid;
	}

	public Agent[] getAgentList() {
		return agentList;
	}

	public Agent[][] getDestinationGrid() {
		return destinationGrid;
	}

	public Agent[][] getCurrentGrid() {
		return currentGrid;
	}

	public Agent getAgent(int x, int y) { return currentGrid[y][x]; }

	public Agent getAgent(Pair<Integer,Integer> position) {return getAgent(position.getKey(),position.getValue());}


	public Agent getAgentDestination(int x, int y) { return destinationGrid[y][x]; }

	public Map<Agent, Pair<Integer, Integer>> getAgentPos() { return agentPos; }

	public int getNbAgent() { return nbAgent; }

	public int getSizeX() { return sizeX; }

	public int getSizeY() { return sizeY; }

	public MailBox getMailBox() {
		return this.mailBox;
	}

	public Map<Agent, Pair<Integer, Integer>> getAgentDestination() { return agentDestination; }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Destination state :\n");
		sb.append(printGrid(this.destinationGrid));

		// Agents
		sb.append("\n\nCurrent state :\n");
		sb.append(printGrid(this.currentGrid));
		return sb.toString();
	}

	public String printGrid(Agent[][] currentGrid) {
		StringBuilder sb = new StringBuilder();
		sb.append('|').append("-".repeat(Math.max(0, sizeX*3)+1)).append("|\n");
		for(int i=0;i<sizeY;i++){
			sb.append("| ");
			for(int j=0;j<sizeX;j++){
				if(currentGrid[i][j]==null) {
					sb.append("   ");
				}else{
					int id = currentGrid[i][j].getID();
					int l = (""+id).length();
					if(l<2) sb.append('0');
					sb.append(id);
					sb.append(' ');
				}
			}
			sb.append("|\n");
		}
		sb.append('|').append("-".repeat(Math.max(0, sizeX*3)+1)).append("|\n");
		/*System.out.println("Agent destination :");
		this.agentDestination.forEach((key, value) -> System.out.print(key.getID() + " " + value+ " / "));
		System.out.println("\nAgent pos :");
		this.agentPos.forEach((key, value) -> System.out.print(key.getID() + " " + value+ " / "));*/
		return sb.toString();
	}

	public boolean isFinished() {
		for(Map.Entry<Agent, Pair<Integer, Integer>> entries : agentPos.entrySet()){
			if(!agentDestination.get(entries.getKey()).equals(entries.getValue())){
				return false;
			}
		}
		return true;
	}

	/**
	 * Move the agent in the given direction.
	 * @param agent The agent to be moved
	 * @param dir The direction (must be checked valid before
	 * @return #TODO
	 */
	public synchronized boolean moveAgent(Agent agent, Direction dir) {
		//System.out.println("Agent "+agent.getID()+" is moving");
		Pair<Integer, Integer> coords = this.agentPos.get(agent);
		int x = coords.getKey();
		int y = coords.getValue();

		Pair<Integer,Integer> newCoords = getPositionFromDirection(coords,dir);
		int newX = newCoords.getKey();
		int newY = newCoords.getValue();
		Agent agentAtNewPos = getAgent(newCoords);
		if(agentAtNewPos!=null){
			System.out.println("Agent " + agent + " tried to move  at "+agentAtNewPos +" place, aborting.");
			return false;
		}
		this.currentGrid[y][x] = null;
		this.currentGrid[newY][newX] = agent;
		this.agentPos.put(agent,newCoords);

		this.moveCountAgent.put(agent,this.moveCountAgent.get(agent)+1);

		setChanged();
		notifyObservers();
		return true;
	}

	/**
	 * @return The total number of steps made by all agents (sum)
	 */
	public int getMoveCount(){
		return this.moveCountAgent.values().stream().mapToInt(Integer::intValue).sum();
	}

	/**
	 * @return The maximum number of steps any agent has made during the resolution.
	 */
	public int getMaxMoveCount(){
		return this.moveCountAgent.values().stream().max(Integer::compare).orElse(-1);
	}

	public boolean isValidPosition(int x, int y){
		return x >= 0 && y >=0 && x < currentGrid[0].length && y < currentGrid.length;
	}

	public boolean isValidDirection(int x,int y,Direction direction){
		return switch (direction) {
			case TOP -> y - 1 >= 0;
			case RIGHT -> x + 1 < currentGrid[0].length;
			case BOTTOM -> y + 1 < currentGrid.length;
			case LEFT -> x - 1 >= 0;
		};
	}

	public boolean isValidDirection(Pair<Integer,Integer>position, Direction direction){
		return isValidDirection(position.getKey(),position.getValue(),direction);
	}

	public Pair<Integer,Integer> getPositionFromDirection(Pair<Integer,Integer> basePosition,Direction direction){
		int x = basePosition.getKey();
		int y = basePosition.getValue();
		switch (direction) {
			case TOP -> y--;
			case RIGHT -> x++;
			case BOTTOM -> y++;
			case LEFT -> x--;
		}
		return new Pair<>(x, y);
	}


	/**
	 * @return The agent in the given direction from the current agent position
	 * @throws Exception if the position in the direction is out of the grid
	 */
	public Agent getAgentInDirection(Agent agent, Direction direction) throws Exception {
		Pair<Integer,Integer> currentPos = this.getAgentPos().get(agent);
		Exception invalidPos = new Exception("Required direction is invalid. Pos : "+currentPos +" / Direction :" +direction);
		if(!isValidDirection(currentPos,direction))
			throw invalidPos;

		Pair<Integer,Integer> nextPos = getPositionFromDirection(currentPos,direction);
		return getAgent(nextPos);
	}


}
