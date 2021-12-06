package com.taquin;



import javafx.util.Pair;

import java.util.*;

/**
 * A class representing the 15puzzle
 * The grid is of size Agent[sizeY][sizeX].
 * So to access an element at position (x,y), use : currentGrid[y][x]
 */
public class Puzzle{
	private Agent[] agentList;
	private Agent[][] destinationGrid;
	private Agent[][] currentGrid;
	private Map<Agent, Pair<Integer,Integer>> agentPos;
	private Map<Agent, Pair<Integer,Integer>> agentDestination;
	private int nbAgent;
	private int sizeX,sizeY;

	public Puzzle(int nbAgent,int sizeX,int sizeY) {
		this.nbAgent = nbAgent;
		this.sizeX = sizeX;
		this.sizeY=sizeY;
		this.agentPos = new HashMap<>();
		this.agentDestination = new HashMap<>();
		agentList = generateAgents();
		currentGrid = generateRandomGrid(this.agentPos);
		destinationGrid = generateRandomGrid(this.agentDestination);
	}

	public void runResolution(){
		for(Agent agent : agentList){
			agent.start();
		}
	}

	private Agent[] generateAgents() {
		Agent[] tempList = new Agent[nbAgent];
		for(int i=0;i<nbAgent;i++){
			tempList[i] = new Agent(i,this);
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

	public Agent getAgent(int i, int j) { return currentGrid[i][j]; }

	public Map<Agent, Pair<Integer, Integer>> getAgentPos() {
		return agentPos;
	}

	public int getNbAgent() {
		return nbAgent;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public Map<Agent, Pair<Integer, Integer>> getAgentDestination() {
		return agentDestination;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Destination state :\n");
		sb.append(printGrid(this.destinationGrid));

		// Agents
		sb.append("\n\nCurrent state :\n");
		sb.append(printGrid(this.currentGrid));
		return sb.toString();
	}

	private String printGrid(Agent[][] currentGrid) {
		StringBuilder sb = new StringBuilder();
		sb.append('|').append("-".repeat(Math.max(0, sizeX*3)+1)).append("|\n");
		for(int i=0;i<sizeX;i++){
			sb.append("| ");
			for(int j=0;j<sizeY;j++){
				if(currentGrid[j][i]==null) {
					sb.append("   ");
				}else{
					int id = currentGrid[j][i].getID();
					int l = (""+id).length();
					if(l<2) sb.append('0');
					sb.append(currentGrid[j][i].getID());
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

	public boolean moveAgent(Agent agent, Direction top) {
		return true;
	}
}
