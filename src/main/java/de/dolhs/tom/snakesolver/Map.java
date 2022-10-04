package de.dolhs.tom.snakesolver;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class Map {

    private final int rows = 10;

    private final int cols = 10;

    private final LinkedList<int[]> snake = new LinkedList<>();

    private int[] food = new int[2];

    private int direction = 0;

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    private final BooleanProperty over = new SimpleBooleanProperty(true);

    private final IntegerProperty speed = new SimpleIntegerProperty(5);

    public Map() {
        snake.add(new int[]{cols / 2 - 2, rows / 2});
        snake.add(new int[]{cols / 2 - 3, rows / 2});
        snake.add(new int[]{cols / 2 - 4, rows / 2});
        food[0] = cols / 2 + 2;
        food[1] = rows / 2;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void move() {
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i)[0] = snake.get(i - 1)[0];
            snake.get(i)[1] = snake.get(i - 1)[1];
        }
        switch (direction) {
            case 0: // Right.
                snake.get(0)[0] = snake.get(0)[0] + 1;
                break;
            case 1: // Up.
                snake.get(0)[1] = snake.get(0)[1] - 1;
                break;
            case 2: // Left.
                snake.get(0)[0] = snake.get(0)[0] - 1;
                break;
            case 3: // Down.
                snake.get(0)[1] = snake.get(0)[1] + 1;
                break;
        }
        collide();
        eat();
    }

    public void eat() {
        if (snake.getFirst()[0] == food[0] && snake.getFirst()[1] == food[1]) {
            snake.addFirst(new int[]{food[0], food[1]});
            score.set(score.get() + 1);
            generateFood();
        }
    }

    private void collide() {
        boolean over = false;
        int[] head = snake.getFirst();
        over |= head[0] < 0 || head[0] >= cols || head[1] < 0 || head[1] >= rows;
        for (int i = 1; i < snake.size(); i++) {
            int[] part = snake.get(i);
            over |= head[0] == part[0] && head[1] == part[1];
        }
        this.over.set(over);
    }

    private void generateFood() {
        Random random = new Random();
        ArrayList<int[]> valid = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                valid.add(new int[]{x, y});
            }
        }
        for (int i = 1; i < snake.size(); i++) {
            valid.set(snake.get(i)[1] * rows + snake.get(i)[0], null);
        }
        valid.removeIf(Objects::isNull);
        if (valid.size() > 0)
            food = valid.get(random.nextInt(valid.size()));
    }

    public void reset() {
        snake.clear();
        snake.add(new int[]{cols / 2 - 2, rows / 2});
        snake.add(new int[]{cols / 2 - 3, rows / 2});
        snake.add(new int[]{cols / 2 - 4, rows / 2});
        food[0] = cols / 2 + 2;
        food[1] = rows / 2;
        direction = 0;
        over.set(false);
        score.set(0);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public LinkedList<int[]> getSnake() {
        return snake;
    }

    public int[] getFood() {
        return food;
    }

    public int getDirection() {
        return direction;
    }

    public BooleanProperty isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over.set(over);
    }

    public IntegerProperty getScore() {
        return score;
    }

    public IntegerProperty getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }

}
