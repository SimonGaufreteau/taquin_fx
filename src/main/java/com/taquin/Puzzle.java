package com.taquin;



import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A class representing the 15puzzle
 * The grid is of size Agent[sizeY][sizeX].
 * So to access an element at position (x,y), use : currentGrid[y][x]
 */
public class Puzzle {
	private Agent[] agentList;
	private Agent[][] destinationGrid;
	private Agent[][] currentGrid;
	private Map<Agent, Pair<Integer,Integer>> agentPos;
	private int nbAgent;
	private int sizeX,sizeY;

	public Puzzle(int nbAgent,int sizeX,int sizeY) {
		this.nbAgent = nbAgent;
		this.sizeX = sizeX;
		this.sizeY=sizeY;
		this.agentPos = new HashMap<>();
		agentList = generateAgents();
		currentGrid = generateRandomGrid();
		destinationGrid = generateRandomGrid();

	}

	private Agent[] generateAgents() {
		Agent[] tempList = new Agent[nbAgent];
		for(int i=0;i<nbAgent;i++){
			tempList[i] = new Agent(i,this);
		}
		return tempList;
	}

	public Agent[][] generateRandomGrid(){
		Agent[][] tempGrid = new Agent[sizeY][sizeX];
		Random random = new Random();
		int agentIndex = 0;
		while(agentIndex<nbAgent){
			int tempX = random.nextInt(sizeX);
			int tempY = random.nextInt(sizeY);
			if(tempGrid[tempY][tempX] == null){
				tempGrid[tempY][tempX] = agentList[agentIndex];
				agentPos.put(agentList[agentIndex],new Pair<>(tempX,tempY));
				agentIndex++;
			}
		}
		return tempGrid;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Environnement :\n");
		sb.append('|').append("-".repeat(Math.max(0, sizeX*3))).append("|\n");
		for(int i=0;i<sizeX;i++){
			sb.append("|");
			for(int j=0;j<sizeY;j++){
				sb.append(' ').append(currentGrid[j][i]==null?" ":currentGrid[j][i].getID()).append(' ');
			}
			sb.append("|\n");
		}
		sb.append('|').append("-".repeat(Math.max(0, sizeX*3))).append('|');

		return sb.toString();
	}
}
