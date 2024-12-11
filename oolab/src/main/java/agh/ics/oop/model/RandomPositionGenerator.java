package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomPositionGenerator implements Iterable<Vector2d> {

    private final List<Vector2d> possible_positions;
    private final int grassCount;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        this.grassCount = grassCount;

        List<Vector2d> all_possible_positions = new ArrayList<>();
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) all_possible_positions.add(new Vector2d(x, y));
        }

        Collections.shuffle(all_possible_positions, new Random());
        this.possible_positions = all_possible_positions.subList(0, grassCount);

    }

    @Override
    public Iterator<Vector2d> iterator() {
        return possible_positions.iterator();

        // losowanie wewnątrz next
    }
}
