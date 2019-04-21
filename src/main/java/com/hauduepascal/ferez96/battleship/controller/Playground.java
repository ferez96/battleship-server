package com.hauduepascal.ferez96.battleship.controller;


import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Playground implements ShipDestroyListener {

    private final int size;
    private final int nRock;
    private final Map<Position, ICell> Playground = new HashMap<>();
    public static final BlankCell BLANK_CELL = new BlankCell();
    public static final Rock ROCK = new Rock();


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

    public void prettyPrint(PrintStream ps) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size; ++i) sb.append("+---");
        sb.append("+");
        String line = sb.toString();
        ps.println(line);
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                ps.printf("|%3s", Playground.get(Position.get(i, j)).toPrettyString());
            }
            ps.println("|");
            ps.println(line);
        }
    }

    @Override
    public void onShipDestroy(Ship ship) {
        Playground.put(ship.pos, BLANK_CELL);
    }

    public interface ICell extends java.io.Serializable {
        String toPrettyString();
    }

    public static class BlankCell implements ICell {
        @Override
        public String toPrettyString() {
            return "   ";
        }
    }

    public static class Rock implements ICell {
        @Override
        public String toPrettyString() {
            return " # ";
        }
    }
}
