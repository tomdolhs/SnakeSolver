package de.dolhs.tom.snakesolver.logic;

import de.dolhs.tom.snakesolver.model.Map;

import java.util.LinkedList;

public abstract class Solver {

    protected Map map;

    public Solver(Map map) {
        this.map = map;
    }

    public abstract void next();

    protected boolean adjacent(int[] a, int[] b) {
        return a[1] == b[1] && a[0] == b[0] - 1 || a[1] == b[1] + 1 && a[0] == b[0] || a[1] == b[1] && a[0] == b[0] + 1 || a[1] == b[1] - 1 && a[0] == b[0];
    }

    protected LinkedList<int[]> adjacent(int[] field) {
        LinkedList<int[]> neighbors = new LinkedList<>();
        if (field[0] + 1 < map.getCols())
            neighbors.add(new int[]{field[0] + 1, field[1]});
        if (field[1] - 1 >= 0)
            neighbors.addLast(new int[]{field[0], field[1] - 1});
        if (field[0] - 1 >= 0)
            neighbors.addLast(new int[]{field[0] - 1, field[1]});
        if (field[1] + 1 < map.getRows())
            neighbors.addLast(new int[]{field[0], field[1] + 1});
        return neighbors;
    }

    protected LinkedList<Integer> directions(LinkedList<int[]> path) {
        LinkedList<Integer> directions = new LinkedList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i)[1] == path.get(i + 1)[1] && path.get(i)[0] == path.get(i + 1)[0] - 1) {
                directions.add(0);
            } else if (path.get(i)[1] == path.get(i + 1)[1] + 1 && path.get(i)[0] == path.get(i + 1)[0]) {
                directions.add(1);
            } else if (path.get(i)[1] == path.get(i + 1)[1] && path.get(i)[0] == path.get(i + 1)[0] + 1) {
                directions.add(2);
            } else if (path.get(i)[1] == path.get(i + 1)[1] - 1 && path.get(i)[0] == path.get(i + 1)[0]) {
                directions.add(3);
            }
        }
        return directions;
    }

}

