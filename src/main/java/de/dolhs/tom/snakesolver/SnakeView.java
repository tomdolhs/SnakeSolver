package de.dolhs.tom.snakesolver;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedList;

public class SnakeView extends Application {

    private final SnakeController controller = new SnakeController(this);

    private GraphicsContext map;

    private Timeline timeline;

    private final int tileSize = 40;

    private final int snakeSize = 20;

    private final int foodSize = 20;

    private boolean directionChanged = false;

    @Override
    public void start(Stage stage) {
        // Stage.
        stage.setTitle("Snake");
        stage.setHeight(controller.getMap().getRows() * tileSize + 73);
        stage.setResizable(false);

        // Scene.
        Canvas canvas = new Canvas(controller.getMap().getCols() * tileSize, controller.getMap().getRows() * tileSize);
        canvas.setFocusTraversable(false);
        map = canvas.getGraphicsContext2D();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createToolbar());
        borderPane.setCenter(canvas);
        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(createControls());
        stage.setScene(scene);
        stage.show();

        // Start game.
        drawBackground();
        drawSnake();
        drawFood();
    }

    private ToolBar createToolbar() {
        // Create toolbar and items.
        ToolBar toolBar = new ToolBar();
        Button play = new Button();
        Text speedText = new Text("Speed:");
        TextField speed = new TextField(String.valueOf(controller.getMap().getSpeed().get()));
        Button increaseSpeed = new Button();
        Button decreaseSpeed = new Button();
        Button solve = new Button();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        Text scoreText = new Text("Score:");
        TextField score = new TextField(String.valueOf(controller.getMap().getScore().get()));

        // Set item appearance.
        int iconSize = 12;
        ImageView playIcon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/" + (controller.getMap().isOver().get() ? "play" : "exit") + ".png").toExternalForm()));
        playIcon.setFitWidth(iconSize);
        playIcon.setFitHeight(iconSize);
        play.setGraphic(playIcon);
        play.setFocusTraversable(false);
        speed.setPrefWidth(30);
        speed.setFocusTraversable(false);
        speed.setEditable(false);
        score.setPrefWidth(50);
        score.setFocusTraversable(false);
        score.setEditable(false);
        ImageView decreaseDifficultyIcon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/minus.png").toExternalForm()));
        decreaseDifficultyIcon.setFitWidth(iconSize);
        decreaseDifficultyIcon.setFitHeight(iconSize);
        decreaseSpeed.setGraphic(decreaseDifficultyIcon);
        decreaseSpeed.setFocusTraversable(false);
        ImageView increaseDifficultyIcon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/plus.png").toExternalForm()));
        increaseDifficultyIcon.setFitWidth(iconSize);
        increaseDifficultyIcon.setFitHeight(iconSize);
        increaseSpeed.setGraphic(increaseDifficultyIcon);
        increaseSpeed.setFocusTraversable(false);
        ImageView solveIcon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/bot.png").toExternalForm()));
        solveIcon.setFitWidth(iconSize);
        solveIcon.setFitHeight(iconSize);
        solve.setGraphic(solveIcon);
        solve.setFocusTraversable(false);

        // Set item actions.
        play.setOnAction(a -> controller.play());
        decreaseSpeed.setOnAction(a -> controller.decreaseSpeed());
        decreaseSpeed.setDisable(!controller.getMap().isOver().get());
        increaseSpeed.setOnAction(a -> controller.increaseSpeed());
        increaseSpeed.setDisable(!controller.getMap().isOver().get());
        solve.setOnAction(a -> controller.solve());

        // Set change listeners.
        controller.getMap().isOver().addListener((observable, oldValue, newValue) -> {
            ImageView icon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/" + (observable.getValue() ? "play" : "exit") + ".png").toExternalForm()));
            icon.setFitWidth(iconSize);
            icon.setFitHeight(iconSize);
            play.setGraphic(icon);
        });
        controller.isSolving().addListener((observable, oldValue, newValue) -> {
            play.setDisable(observable.getValue());
            ImageView icon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/" + (!observable.getValue() ? "bot" : "exit") + ".png").toExternalForm()));
            icon.setFitWidth(iconSize);
            icon.setFitHeight(iconSize);
            solve.setGraphic(icon);
        });
        controller.getMap().getSpeed().addListener((observable, oldValue, newValue) -> speed.setText(String.valueOf(observable.getValue())));
        controller.getMap().getScore().addListener((observable, oldValue, newValue) -> score.setText(String.valueOf(observable.getValue())));

        toolBar.getItems().addAll(play, new Separator(), speedText, speed, decreaseSpeed, increaseSpeed, new Separator(), solve, new Separator(), spacer, scoreText, score);
        return toolBar;
    }

    private EventHandler<KeyEvent> createControls() {
        return keyEvent -> {
            if (!directionChanged && !controller.isSolving().get()) {
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    controller.setDirection(0);
                    directionChanged = true;
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    controller.setDirection(1);
                    directionChanged = true;
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    controller.setDirection(2);
                    directionChanged = true;
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    controller.setDirection(3);
                    directionChanged = true;
                }
            }
        };
    }

    public void draw() {
        directionChanged = false;
        controller.move();
        drawBackground();
        drawSnake();
        drawFood();
    }

    private void drawBackground() {
        for (int x = 0; x < controller.getMap().getRows(); x++) {
            for (int y = 0; y < controller.getMap().getCols(); y++) {
                map.setFill(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1 ? Color.rgb(118, 186, 27) : Color.rgb(164, 222, 2));
                map.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }

    private void drawSnake() {
        map.setFill(Color.rgb(53, 65, 165));
        for (int i = 0; i < controller.getMap().getSnake().size(); i++) {
            LinkedList<int[]> snake = controller.getMap().getSnake();
            if (i > 0) {
                if (snake.get(i)[1] == snake.get(i - 1)[1] && snake.get(i)[0] == snake.get(i - 1)[0] + 1) {
                    map.fillRect(snake.get(i)[0] * tileSize, snake.get(i)[1] * tileSize + (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize));
                } else if (snake.get(i)[0] == snake.get(i - 1)[0] && snake.get(i)[1] == snake.get(i - 1)[1] - 1) {
                    map.fillRect(snake.get(i)[0] * tileSize + (tileSize - snakeSize) / 2, snake.get(i)[1] * tileSize + (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize), tileSize - (tileSize - snakeSize) / 2);
                } else if (snake.get(i)[1] == snake.get(i - 1)[1] && snake.get(i)[0] == snake.get(i - 1)[0] - 1) {
                    map.fillRect(snake.get(i)[0] * tileSize + (tileSize - snakeSize) / 2, snake.get(i)[1] * tileSize + (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize));
                } else if (snake.get(i)[0] == snake.get(i - 1)[0] && snake.get(i)[1] == snake.get(i - 1)[1] + 1) {
                    map.fillRect(snake.get(i)[0] * tileSize + (tileSize - snakeSize) / 2, snake.get(i)[1] * tileSize, tileSize - (tileSize - snakeSize), tileSize - (tileSize - snakeSize) / 2);
                }
            } if (i < snake.size() - 1) {
                if (snake.get(i)[1] == snake.get(i + 1)[1] && snake.get(i)[0] == snake.get(i + 1)[0] + 1) {
                    map.fillRect(snake.get(i)[0] * tileSize, snake.get(i)[1] * tileSize + (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize));
                } else if (snake.get(i)[0] == snake.get(i + 1)[0] && snake.get(i)[1] == snake.get(i + 1)[1] - 1) {
                    map.fillRect(snake.get(i)[0] * tileSize + (tileSize - snakeSize) / 2, snake.get(i)[1] * tileSize + (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize), tileSize - (tileSize - snakeSize) / 2);
                } else if (snake.get(i)[1] == snake.get(i + 1)[1] && snake.get(i)[0] == snake.get(i + 1)[0] - 1) {
                    map.fillRect(snake.get(i)[0] * tileSize + (tileSize - snakeSize) / 2, snake.get(i)[1] * tileSize + (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize) / 2, tileSize - (tileSize - snakeSize));
                } else if (snake.get(i)[0] == snake.get(i + 1)[0] && snake.get(i)[1] == snake.get(i + 1)[1] + 1) {
                    map.fillRect(snake.get(i)[0] * tileSize + (tileSize - snakeSize) / 2, snake.get(i)[1] * tileSize, tileSize - (tileSize - snakeSize), tileSize - (tileSize - snakeSize) / 2);
                }
            }
        }
    }

    private void drawFood() {
        map.setFill(Color.rgb(255, 51, 51));
        int[] food = controller.getMap().getFood();
        map.fillRect(food[0] * tileSize + (tileSize - foodSize) / 2, food[1] * tileSize + (tileSize - foodSize) / 2, foodSize, foodSize);
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public static void main(String[] args) {
        launch();
    }

}