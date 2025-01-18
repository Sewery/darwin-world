package agh.ics.oop.model.util;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.WorldMap;

public interface MapChangeListener {

    void mapChanged(WorldMap map, String message);
}