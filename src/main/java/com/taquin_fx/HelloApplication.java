package com.taquin_fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class HelloApplication extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws IOException {
		//FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
		//Scene scene = new Scene(fxmlLoader.load(), 320, 240);
		//stage.setTitle("Hello!");
		//stage.setScene(scene);
		//.show();

		Rectangle r1 = new Rectangle();
		r1.setX(200);
		r1.setY(200);
		r1.setWidth(200);
		r1.setHeight(200);
		r1.setFill(Color.ALICEBLUE);
		
		Pane pane = new Pane();
		pane.getChildren().add(r1);

		Scene sc = new Scene(pane, 1024, 800, true);
		stage.setScene(sc);
		stage.setTitle("TAQUIN très TAQUIN");

		stage.show();


	}


}