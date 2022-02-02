package com.taquin_fx;

import com.taquin.Agent;
import com.taquin.Puzzle;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.*;


public class HelloApplication extends Application implements Observer {

	private final Puzzle pz;
	private final int WINSIZEX = 800;
	private final int WINSIZEY = 800;
	private final double TILE_RATIO = 4./5.;
	private HashMap<Agent, Color> colorMap;
	private HashMap<Rectangle, Pair<Integer, Integer>> rectCoord;
	private GridPane gP;
	private Stage stage;
	private Scene sc;
	//25 distinct colors -- credit to https://mokole.com/palette.html
	private final Color[] colorsList = {
			Color.BLACK,
			Color.LIGHTGRAY,
			Color.DARKOLIVEGREEN,
			Color.SADDLEBROWN,
			Color.MEDIUMSEAGREEN,
			Color.DARKCYAN,
			Color.YELLOWGREEN,
			Color.DARKBLUE,
			Color.DARKMAGENTA,
			Color.ORANGERED,
			Color.ORANGE,
			Color.YELLOW,
			Color.LAWNGREEN,
			Color.SPRINGGREEN,
			Color.CRIMSON,
			Color.AQUA,
			Color.BLUE,
			Color.FUCHSIA,
			Color.PALEVIOLETRED,
			Color.KHAKI,
			Color.CORNFLOWERBLUE,
			Color.DEEPPINK,
			Color.MEDIUMSLATEBLUE,
			Color.LIGHTSALMON,
			Color.VIOLET
	};

	public HelloApplication(Puzzle pz) {
		super();
		this.pz = pz;
		colorMap = new HashMap<>();
		rectCoord = new HashMap<>();
	}

	@Override
	public void update(Observable o, Object arg) {
		Platform.runLater(() -> {

			//Close app 5 seconds after puzzle completion
			if(pz.isFinished()) {
				try {
					System.out.printf("Taquin solved in %d moves\n",pz.getMaxMoveCount());
					PauseTransition delay = new PauseTransition(Duration.seconds(5));
					delay.setOnFinished( event ->{
						pz.reset();
						updateColorMap();
						pz.runResolution();

					}  );
					delay.play();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for(Node n : gP.getChildren()) {
				Rectangle rec = (Rectangle) n;
				Pair<Integer, Integer> coords = rectCoord.get(rec);
				Agent agent = pz.getAgent(coords.getKey(), coords.getValue());
				rec.setFill(agent!=null ? colorMap.get(agent) : Color.WHITE);
			}

		});
	}

	/**
	 * Updates the color map with every agent. The colors stay the same after a puzzle reset only if the reset is done
	 * in the same order.
	 */
	private void updateColorMap() {
		int colCount = 0;
		Agent[] agentList = pz.getAgentList();
		for (Agent agent : agentList) {
			Color tileColor = colorsList[colCount];
			colCount++;
			colCount = colCount >= colorsList.length ? 0 : colCount;

			colorMap.put(agent, tileColor);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		gP = new GridPane();
		this.stage = stage;
		//Colors
		Color tileColor;
		int colCount = 0;

		//Useful variables
		Agent[][] cR = pz.getCurrentGrid();
		int sizeX = pz.getSizeX();
		int sizeY = pz.getSizeY();

		int tileSizeX = (int) ((WINSIZEX/sizeX)*TILE_RATIO);
		int tileSizeY = (int) ((WINSIZEY/sizeY)*TILE_RATIO);

		int stroke_width = (WINSIZEX/sizeX)-tileSizeX;

		updateColorMap();

		for (int row = 0; row < sizeX; row++) {
			for (int col = 0; col < sizeY; col++) {
				Rectangle rec = new Rectangle();
				Agent agent = pz.getAgent(row, col);

				//Color
				if(agent != null) {
					tileColor = colorMap.get(agent);
				} else tileColor = Color.WHITE;

				Pair<Integer,Integer> pair = new Pair<>(col, row);
				rectCoord.put(rec, pair);

				//Size && fill
				rec.setWidth(tileSizeY);
				rec.setHeight(tileSizeX);
				rec.setFill(tileColor);

				gP.add(rec, col, row);
			}
		}

		//Borders
		for(Node n : gP.getChildren()) {
			Rectangle rec = (Rectangle) n;
			Pair<Integer, Integer> coords = rectCoord.get(rec);
			Agent agent = pz.getAgentDestination(coords.getKey(), coords.getValue());
			rec.setStroke(agent!=null ? colorMap.get(agent) : Color.WHITE);
			rec.setStrokeWidth(stroke_width);
		}

		sc = new Scene(gP, WINSIZEX, WINSIZEY, true);
		stage.setScene(sc);
		stage.setTitle("Taquin");

		stage.show();

	}
}