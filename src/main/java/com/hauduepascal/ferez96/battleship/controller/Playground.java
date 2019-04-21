package com.hauduepascal.ferez96.battleship.controller;

import java.util.HashMap;
import java.util.Map;

public class Playground {

    private final int size;
    private final int nRock;
    private final Map<Position, ICell> Playground = new HashMap<>();

    public Playground(int size, int nRock) {
        this.size = size;
        this.nRock = nRock;
        for (int i = 1; i <= size; ++i)
            for (int j = 1; j <= size; ++j)
                this.Playground.put(Position.get(i, j), new BlankCell());
    }

    public ICell get(Position pos) {
        return Playground.getOrDefault(pos, null);
    }

    public ICell set(Position pos, ICell cell) {
        return Playground.put(pos, cell);
    }

    public int getSize() {
        return size;
    }

    public int getRockCount() {
        return nRock;
    }

    public interface ICell extends java.io.Serializable {
    }

    public static class BlankCell implements ICell {
        @Override
        public String toString() {
            return ".";
        }
    }

    public static class Rock implements ICell {
        @Override
        public String toString() {
            return "#";
        }
    }
}
