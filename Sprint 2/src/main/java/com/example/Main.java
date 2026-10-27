package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {
    private SolitaireGame game;
    private GridPane boardPane;
    private Label statusLabel;
    private ComboBox<Integer> sizeBox;
    private ComboBox<BoardType> typeBox;

    @Override
    public void start(Stage stage) {
        game = new SolitaireGame(7, BoardType.ENGLISH);

        Label title = new Label("Peg Solitaire");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll(5, 7, 9);
        sizeBox.setValue(7);

        typeBox = new ComboBox<>();
        typeBox.getItems().addAll(BoardType.values());
        typeBox.setValue(BoardType.ENGLISH);

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> startNewGame());

        HBox controls = new HBox(12,
                new Label("Board size:"), sizeBox,
                new Label("Board type:"), typeBox,
                newGameButton);
        controls.setAlignment(Pos.CENTER);

        boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setHgap(5);
        boardPane.setVgap(5);

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 15px;");

        VBox root = new VBox(15, title, controls, boardPane, statusLabel);
        root.setStyle("-fx-background-color: black;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        refreshBoard();

        Scene scene = new Scene(root, 650, 700);
        stage.setTitle("Peg Solitaire");
        stage.setScene(scene);
        stage.show();
    }

    private void startNewGame() {
        game.newGame(sizeBox.getValue(), typeBox.getValue());
        refreshBoard();
    }

    private void refreshBoard() {
        boardPane.getChildren().clear();
        int size = game.getBoard().getSize();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!game.getBoard().isValid(row, col)) {
                    continue;
                }

                Button space = new Button();
                space.setPrefSize(55, 55);

                if (game.getBoard().isOccupied(row, col)) {
                    Circle peg = new Circle(18);
                    space.setGraphic(peg);
                } else {
                    space.setText("•");
                }

                Position selected = game.getSelected();
                if (selected != null && selected.row() == row && selected.col() == col) {
                    space.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                }

                final int r = row;
                final int c = col;
                space.setOnAction(e -> handleBoardClick(r, c));

                boardPane.add(space, col, row);
            }
        }

        if (game.isGameOver()) {
            statusLabel.setText("No legal moves left. Game Over. Pegs left: "
                    + game.getBoard().getPegCount());
        } else {
            statusLabel.setText("Pegs left: " + game.getBoard().getPegCount()
                    + " | Select a peg, then select a destination.");
        }
    }

    private void handleBoardClick(int row, int col) {
        if (game.getSelected() == null) {
            if (!game.select(row, col)) {
                statusLabel.setText("Select an occupied peg first.");
            }
        } else {
            if (!game.moveSelectedTo(row, col)) {
                if (game.select(row, col)) {
                } else {
                    statusLabel.setText("Invalid move. A peg must jump over another peg.");
                }
            }
        }

        refreshBoard();
    }

    public static void main(String[] args) {
        launch();
    }
}
