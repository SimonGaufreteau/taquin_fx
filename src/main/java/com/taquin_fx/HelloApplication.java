package com.taquin_fx;

import com.taquin.Agent;
import com.taquin.Puzzle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class HelloApplication extends Application implements Observer {

	private final Puzzle pz;
	HashMap<Agent, Color> colorMap;
	HashMap<Rectangle, Pair<Integer, Integer>> rectCoord;
	GridPane gP;
	Scene sc;
	private final Color[] colorsList = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.YELLOW, Color.ORANGE, Color.BROWN};

	public HelloApplication(Puzzle pz) {
		super();
		this.pz = pz;
		colorMap = new HashMap<>();
		rectCoord = new HashMap<>();
	}

	//TODO Swap les param de la hashmap pour itérer sur le childset et plus avoir àle clear -- passer en css -- voir pour use des region pour les border
	@Override
	public void update(Observable o, Object arg) {
		Platform.runLater(() -> {
			List<Node> children = gP.getChildren();
			//gP.getChildren().clear();
			//System.out.println("Updating UI");

			//Useful variables
			Agent[][] cR = pz.getCurrentGrid();
			System.out.println(pz);

			for(Node n : gP.getChildren()) {
				Rectangle rec = (Rectangle) n;
				Pair<Integer, Integer> coords = rectCoord.get(rec);
				Agent agent = pz.getAgent(coords.getKey(), coords.getValue());
				rec.setFill(agent!=null ? colorMap.get(agent) : Color.WHITE);
			}


			/*for(Pair<Integer, Integer> coord : rectCoord.keySet()) {
				Rectangle rec = rectCoord.get(coord);
				if(cR[coord.getKey()][coord.getValue()] != null) {
					Agent agent = pz.getAgent(coord.getKey(), coord.getValue());
					rec.setFill(colorMap.get(agent));
				}else{
					rec.setFill(Color.WHITE);
				}
				gP.add(rec, coord.getKey(), coord.getValue());
			}*/
		});
	}

	@Override
	public void start(Stage stage) throws Exception {
		gP = new GridPane();

		//Colors
		Color tileColor;
		int colCount = 0;

		//Useful variables
		Agent[][] cR = pz.getCurrentGrid();
		int nbAgent = pz.getNbAgent();
		int sizeX = pz.getSizeX();
		int sizeY = pz.getSizeY();

		int tileSizeX = 1024/sizeX;
		int tileSizeY = 1024/sizeY;

		for (int row = 0; row < sizeX; row++) {
			for (int col = 0; col < sizeY; col++) {
				Rectangle rec = new Rectangle();

				//Color
				if(cR[col][row] != null) {
					tileColor = colorsList[colCount];
					colCount++;
					if (colCount == 8) {
						colCount=0;
					}
					Agent agent = pz.getAgent(row, col);
					colorMap.put(agent, tileColor);
				} else tileColor = Color.WHITE;

				Pair pair = new Pair(col, row);
				rectCoord.put(rec, pair);

				//Size && fill
				rec.setWidth(tileSizeY);
				rec.setHeight(tileSizeX);
				rec.setFill(tileColor);


				//GridPane.setRowIndex(rec, row);
				//GridPane.setColumnIndex(rec, col);
				gP.add(rec, col, row);
			}
		}

		sc = new Scene(gP, 1024, 1024, true);
		stage.setScene(sc);
		stage.setTitle("TAQUIN très TAQUIN");

		stage.show();

	}
}