package de.dolhs.tom.snakesolver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

public class SnakeController {

    private final SnakeView snakeView;

    private final Map map = new Map();

    private Solver solver = null;

    private BooleanProperty solving = new SimpleBooleanProperty(false);

    public SnakeController(SnakeView snakeView) {
        this.snakeView = snakeView;
    }

    public Map getMap() {
        return map;
    }

    public void move() {
        if (!map.isOver().get()) {
            if (solving.get())
                solver.next();
            map.move();
        } else {
            solver = null;
            solving.set(false);
            snakeView.getTimeline().stop();
        }
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
            snakeView.setTimeline(new Timeline(new KeyFrame(Duration.millis(Math.max(250 - getMap().getSpeed().get() * 24, 1)), e -> snakeView.draw())));
            snakeView.getTimeline().setCycleCount(Animation.INDEFINITE);
            snakeView.getTimeline().play();
        }
    }

    public void solve() {
        if (!map.isOver().get())
            map.setOver(true);
        else {
            map.reset();
            solver = new Solver(map);
            solving.set(true);
            snakeView.setTimeline(new Timeline(new KeyFrame(Duration.millis(Math.max(250 - getMap().getSpeed().get() * 24, 1)), e -> snakeView.draw())));
            snakeView.getTimeline().setCycleCount(Animation.INDEFINITE);
            snakeView.getTimeline().play();
        }
    }

    public void increaseSpeed() {
        map.setSpeed(Math.min(10, map.getSpeed().get() + 1));
    }

    public void decreaseSpeed() {
        map.setSpeed(Math.max(1, map.getSpeed().get() - 1));
    }

    public BooleanProperty isSolving() {
        return solving;
    }

}
