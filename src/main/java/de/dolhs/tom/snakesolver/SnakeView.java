package de.dolhs.tom.snakesolver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SnakeView extends Application {

    private final SnakeController controller = new SnakeController(this);

    private Stage stage;

    private GraphicsContext map;

    private ToolBar toolBar;

    private BorderPane borderPane;

    private Timeline timeline;

    private int tileSize = 10;

    @Override
    public void start(Stage stage) {
        // Stage.
        this.stage = stage;
        stage.setTitle("Snake");
        stage.setHeight(controller.getMap().getRows() * 10 + 73);
        stage.setResizable(false);

        // Scene.
        Canvas canvas = new Canvas(controller.getMap().getCols() * tileSize, controller.getMap().getRows() * tileSize);
        map = canvas.getGraphicsContext2D();
        borderPane = new BorderPane();
        borderPane.setTop(createToolbar());
        borderPane.setCenter(canvas);
        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(createControls());
        stage.setScene(scene);
        stage.show();

        // Start game.
        timeline = new Timeline(new KeyFrame(Duration.millis(250 - controller.getMap().getDifficulty().get() * 20), e -> draw()));
        timeline.setCycleCount(Animation.INDEFINITE);
        drawBackground();
        drawSnake();
        drawFood();
    }

    private ToolBar createToolbar() {
        // Create toolbar and buttons.
        ToolBar toolBar = new ToolBar();
        Button play = new Button();
        Text difficultyText = new Text("Difficulty:");
        TextField difficulty = new TextField(String.valueOf(controller.getMap().getDifficulty().get()));
        Button increaseDifficulty = new Button();
        Button decreaseDifficulty = new Button();
        Text scoreText = new Text("Score:");
        TextField score = new TextField(String.valueOf(controller.getMap().getScore().get()));

        // Set button icons.
        int iconSize = 10;
        ImageView playIcon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/" + (controller.getMap().isOver().get() ? "play" : "exit") + ".png").toExternalForm()));
        playIcon.setFitWidth(iconSize);
        playIcon.setFitHeight(iconSize);
        play.setGraphic(playIcon);
        play.setFocusTraversable(false);
        difficulty.setPrefWidth(30);
        difficulty.setFocusTraversable(false);
        difficulty.setEditable(false);
        score.setPrefWidth(50);
        score.setFocusTraversable(false);
        score.setEditable(false);
        ImageView increaseDifficultyIcon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/plus.png").toExternalForm()));
        increaseDifficultyIcon.setFitWidth(iconSize);
        increaseDifficultyIcon.setFitHeight(iconSize);
        increaseDifficulty.setGraphic(increaseDifficultyIcon);
        increaseDifficulty.setFocusTraversable(false);
        ImageView decreaseDifficultyIcon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/minus.png").toExternalForm()));
        decreaseDifficultyIcon.setFitWidth(iconSize);
        decreaseDifficultyIcon.setFitHeight(iconSize);
        decreaseDifficulty.setGraphic(decreaseDifficultyIcon);
        decreaseDifficulty.setFocusTraversable(false);

        // Set button actions.
        play.setOnAction(a -> controller.play());
        increaseDifficulty.setOnAction(a -> controller.increaseDifficulty());
        increaseDifficulty.setDisable(!controller.getMap().isOver().get());
        decreaseDifficulty.setOnAction(a -> controller.decreaseDifficulty());
        decreaseDifficulty.setDisable(!controller.getMap().isOver().get());

        // Set change listeners.
        controller.getMap().isOver().addListener((observable, oldValue, newValue) -> {
            increaseDifficulty.setDisable(!observable.getValue());
            decreaseDifficulty.setDisable(!observable.getValue());
            ImageView icon = new ImageView((getClass().getResource("/de/dolhs/tom/snakesolver/" + (observable.getValue() ? "play" : "exit") + ".png").toExternalForm()));
            icon.setFitWidth(iconSize);
            icon.setFitHeight(iconSize);
            play.setGraphic(icon);
        });
        controller.getMap().getDifficulty().addListener((observable, oldValue, newValue) -> difficulty.setText(String.valueOf(observable.getValue())));
        controller.getMap().getScore().addListener((observable, oldValue, newValue) -> score.setText(String.valueOf(observable.getValue())));

        toolBar.getItems().addAll(play, new Separator(), difficultyText, difficulty, decreaseDifficulty, increaseDifficulty, new Separator(), scoreText, score);
        return toolBar;
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

    public void draw() {
        controller.move();
        createToolbar();
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
        for (int[] part : controller.getMap().getSnake()) {
            map.fillRect(part[0] * tileSize + 1, part[1] * tileSize + 1, tileSize - 1, tileSize - 1);
        }
    }

    private void drawFood() {
        map.setFill(Color.rgb(255, 51, 51));
        int[] food = controller.getMap().getFood();
        map.fillRect(food[0] * tileSize + 1, food[1] * tileSize + 1, tileSize - 1, tileSize);
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