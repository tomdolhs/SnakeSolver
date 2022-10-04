package de.dolhs.tom.snakesolver.logic;

import de.dolhs.tom.snakesolver.model.Map;

import java.util.LinkedList;

public class Hamilton extends Solver {

    private LinkedList<Integer> directions = new LinkedList<>();

    public Hamilton(Map map) {
        super(map);
        hamiltonCycle();
    }

    private void hamiltonCycle() {
        LinkedList<int[]> path = new LinkedList<>();
        path.add(map.getSnake().getFirst());
        hamiltonCycle(path);
        directions.addAll(directions(path));
    }

    private boolean hamiltonCycle(LinkedList<int[]> path) {
        if (path.size() == map.getCols() * map.getRows()) {
            if (adjacent(path.getFirst(), path.getLast())) {
                path.addLast(path.getFirst());
                return true;
            } else
                return false;
        }
        for (int[] neighbor : adjacent(path.getLast())) {
            boolean contained = false;
            for (int[] node : path) {
                contained |= node[0] == neighbor[0] && node[1] == neighbor[1];
            }
            if (!contained && neighbor[0] >= 0 && neighbor[0] < map.getCols() && neighbor[1] >= 0 && neighbor[1] < map.getRows()) {
                path.addLast(neighbor);
                if (hamiltonCycle(path))
                    return true;
                path.removeLast();
            }
        }
        return false;
    }

    @Override
    public void next() {
        map.setDirection(directions.getFirst());
        directions.addLast(directions.removeFirst());
    }

}
