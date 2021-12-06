package com.taquin;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Puzzle puzzle = new Puzzle(6,5,5);
        System.out.println(puzzle);
        puzzle.runResolution();


    }
}
