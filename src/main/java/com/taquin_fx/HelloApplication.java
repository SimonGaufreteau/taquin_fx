package com.taquin_fx;

import com.taquin.Agent;
import com.taquin.Puzzle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class HelloApplication extends Application implements Observer {

	private Puzzle pz;
	HashMap<Agent, Color> colorMap;
	GridPane gP;

	public HelloApplication(Puzzle pz) {
		super();
		this.pz = pz;
		colorMap = new HashMap<>();
	}


	@Override
	public void update(Observable o, Object arg) {
		//Appelée quand setchanged into notifyobservers
		//On applique les changements du model à l'UI
		//Comment update l'UI ? Dupli code ok je suppose ?
		//ca m'a pété les couilles battez vous, soit les agents sont chacun observables, soit le puzlle l'est et les agents doivent notifier le puzzle

		//Colors
		Color[] colorsList = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.YELLOW, Color.ORANGE, Color.BROWN};
		Color tileColor = null;
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
					tileColor = colorMap.get(pz.getAgent(col,row));
				} else tileColor = Color.WHITE;

				//Size && fill
				rec.setWidth(tileSizeY);
				rec.setHeight(tileSizeX);
				rec.setFill(tileColor);


				GridPane.setRowIndex(rec, row);
				GridPane.setColumnIndex(rec, col);
				gP.getChildren().addAll(rec);
			}
		}


	}

	@Override
	public void start(Stage stage) throws Exception {
		gP = new GridPane();

		//Colors
		Color[] colorsList = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.YELLOW, Color.ORANGE, Color.BROWN};
		Color tileColor = null;
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
					colorMap.put(pz.getAgent(col, row), tileColor);
				} else tileColor = Color.WHITE;

				//Size && fill
				rec.setWidth(tileSizeY);
				rec.setHeight(tileSizeX);
				rec.setFill(tileColor);


				GridPane.setRowIndex(rec, row);
				GridPane.setColumnIndex(rec, col);
				gP.getChildren().addAll(rec);
			}
		}

		Scene sc = new Scene(gP, 1024, 1024, true);
		stage.setScene(sc);
		stage.setTitle("TAQUIN très TAQUIN");

		stage.show();

	}
}