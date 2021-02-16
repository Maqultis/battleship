package com.kodilla.battleship;

import java.lang.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;

public class Battleship extends Application {

    private final Image imageback = new Image("file:src/main/resources/background.png");
    private boolean running = false;
    private Board enemyBoard, playerBoard;

    private int shipsToPlace = 5;
    private int enemyShips = enemyBoard != null?enemyBoard.ships :5;
    private int yourShips = playerBoard != null?playerBoard.ships :5;
    private boolean vert = true;


    private boolean enemyTurn = false;

    private Label enemyShipsLabel = new Label("Enemy Ships: " + enemyShips);
    private Label yourShipsLabel = new Label("Your Ships: 5");
    private Label status = new Label("Place your ships");
    private Button verticalChangeButton = new Button("Place ship vertical");


    private final Random random = new Random();


    private Parent createContent() {

        BorderPane border = new BorderPane();
        border.setPrefSize(1200, 600);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true,
                true, true, false);
        border.setBackground(new Background(new BackgroundImage(imageback,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize)));
        border.setRight(yourShipsLabel);
        border.setAlignment(yourShipsLabel, Pos.CENTER_RIGHT);
        yourShipsLabel.setTextFill(Color.WHITE);
        yourShipsLabel.setFont(new Font("Arial", 24));
        border.setLeft(enemyShipsLabel);
        border.setAlignment(enemyShipsLabel, Pos.CENTER_LEFT);
        enemyShipsLabel.setTextFill(Color.WHITE);
        enemyShipsLabel.setFont(new Font("Arial", 24));
        border.setTop(status);
        border.setAlignment(status, Pos.TOP_CENTER);
        status.setTextFill(Color.BLACK);
        status.setFont(new Font("Arial", 24));


        border.setBottom(verticalChangeButton);
        border.setAlignment(verticalChangeButton, Pos.BOTTOM_CENTER);
        verticalChangeButton.setOnMouseClicked(event -> {
            if (verticalChangeButton.getText() == "Place ship vertical") {
                vert = false;
                verticalChangeButton.setText("Place ship horizontal");
            } else if (verticalChangeButton.getText() == "Place ship horizontal") {
                vert = true;
                verticalChangeButton.setText("Place ship vertical");
            }
        });



        enemyBoard = new Board(true, event -> {
            if (!running)
                return;
            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            enemyTurn = !cell.shoot();

            if (enemyBoard.ships < enemyShips) {
                enemyShips--;
                enemyShipsLabel.setText("Enemy Ships: " + enemyBoard.ships);
            } else if (enemyBoard.ships == 0) {
                enemyShipsLabel.setText("Enemy Ships: " + enemyBoard.ships);
                status.setText("YOU WON!");
                System.out.println("YOU WON!");

            }
            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();

            if (playerBoard.placeShip(new Ship(shipsToPlace, vert), cell.x, cell.y)) {
                if (--shipsToPlace == 0) {

                    status.setText("Your Move");
                    startGame();
                }
            }
        });

        HBox hbox = new HBox(50, enemyBoard, playerBoard);
        hbox.setAlignment(Pos.CENTER);

        border.setCenter(hbox);
        return border;
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();

            if (playerBoard.ships < yourShips) {
                yourShips--;
                yourShipsLabel.setText("Your Ships: " + playerBoard.ships);
            } else if (playerBoard.ships == 0) {
                yourShipsLabel.setText("Your Ships: " + playerBoard.ships);
                System.out.println("YOU LOSE");
                status.setText("YOU lOSE!");
                System.exit(0);
            }
        }
    }

    private void startGame() {
        //place enemy
        verticalChangeButton.setVisible(false);
        int type = 5;

        while(type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeShip(new Ship(type, random.nextDouble() < 0.5), x, y)) {
                type--;
            }
        }
        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
