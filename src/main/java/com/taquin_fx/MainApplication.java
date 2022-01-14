package com.taquin_fx;

import com.taquin.Puzzle;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    public static void main(String[] args) { launch(); }

    @Override
    public void start(Stage stage) throws Exception {
        Puzzle pz = new Puzzle(5,5,5,"messageStrategy");
        Stage st = new Stage();
        HelloApplication happ = new HelloApplication(pz);

        pz.addObserver(happ);
        happ.start(st);
        pz.runResolution();
    }
}
