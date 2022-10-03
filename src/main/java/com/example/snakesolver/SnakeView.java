package com.example.snakesolver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SnakeView extends Application {

    private final SnakeController controller = new SnakeController(this);

    private Stage stage;

    private GraphicsContext map;

    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        // Stage.
        this.stage = stage;
        stage.setTitle("Snake");
        stage.setResizable(false);

        // Scene.
        Canvas canvas = new Canvas(500, 500);
        map = canvas.getGraphicsContext2D();
        ToolBar toolbar = createToolbar();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);
        borderPane.setCenter(canvas);
        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(createControls());
        stage.setScene(scene);
        stage.show();

        // Start game.
        timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> draw()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private ToolBar createToolbar() {
        // Create toolbar and buttons.
        return new ToolBar();
    }

    private EventHandler<KeyEvent> createControls() {
        return keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.RIGHT || code == KeyCode.D)
                controller.setDirection(0);
            else if (code == KeyCode.UP || code == KeyCode.W)
                controller.setDirection(1);
            else if (code == KeyCode.LEFT || code == KeyCode.A)
                controller.setDirection(2);
            else if (code == KeyCode.DOWN || code == KeyCode.S)
                controller.setDirection(3);
        };
    }

    private void draw() {
        map.clearRect(0, 0, 500, 500);
        controller.move();
        drawSnake();
        drawFood();
    }

    private void drawSnake() {
        map.setFill(Color.GREEN);
        for (int[] part : controller.getMap().getSnake()) {
            map.fillRect(part[0] * 10 + 1, part[1] * 10 + 1, 9, 9);
        }
    }

    private void drawFood() {
        map.setFill(Color.RED);
        int[] food = controller.getMap().getFood();
        map.fillRect(food[0] * 10 + 1, food[1] * 10 + 1, 9, 9);
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public static void main(String[] args) {
        launch();
    }

}