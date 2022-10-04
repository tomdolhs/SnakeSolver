package de.dolhs.tom.snakesolver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SnakeController {

    private final SnakeView snakeView;

    private final Map map = new Map();

    public SnakeController(SnakeView snakeView) {
        this.snakeView = snakeView;
    }

    public Map getMap() {
        return map;
    }

    public void move() {
        if (!map.isOver().get())
            map.move();
        else
            snakeView.getTimeline().stop();
    }

    public void setDirection(int direction) {
        if (map.getDirection() != (direction + 2) % 4) {
            map.setDirection(direction);
        }
    }

    public void play() {
        if (!map.isOver().get())
            map.setOver(true);
        else {
            map.reset();
            snakeView.setTimeline(new Timeline(new KeyFrame(Duration.millis(250 - getMap().getDifficulty().get() * 20), e -> snakeView.draw())));
            snakeView.getTimeline().setCycleCount(Animation.INDEFINITE);
            snakeView.getTimeline().play();
        }
    }

    public void increaseDifficulty() {
        map.setDifficulty(Math.min(10, map.getDifficulty().get() + 1));
    }

    public void decreaseDifficulty() {
        map.setDifficulty(Math.max(1, map.getDifficulty().get() - 1));
    }

}
