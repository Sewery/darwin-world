package agh.ics.oop.model.util;

import agh.ics.oop.model.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {

    private int changesCount = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {

            changesCount++;
            System.out.println("Map ID: " + worldMap.getID() + ", Map changes count: " + changesCount);
            System.out.println(message);
            System.out.println(worldMap);
        }
    }
}
