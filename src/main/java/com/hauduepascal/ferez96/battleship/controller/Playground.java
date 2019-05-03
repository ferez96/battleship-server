package com.hauduepascal.ferez96.battleship.controller;


import com.hauduepascal.ferez96.battleship.controller.cmd.ICommand;
import com.hauduepascal.ferez96.battleship.controller.cmd.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Playground implements ShipDestroyListener {

    public static final BlankCell BLANK_CELL = new BlankCell();
    public static final Rock ROCK = new Rock();
    private static final Logger Log = LoggerFactory.getLogger(Playground.class);

    public final Position[] Rocks;
    private final int size;
    private final Map<Position, ICell> Playground = new HashMap<>();

    public Playground(int size, Position[] rocks) {
        this.size = size;
        this.Rocks = rocks;
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                Playground.put(Position.get(i, j), BLANK_CELL);
            }
        }
        for (Position pos : rocks) Playground.put(pos, ROCK);
    }

    public ICell get(Position pos) {
        return Playground.getOrDefault(pos, null);
    }

    public ICell set(Position pos, ICell cell) {
        return Playground.put(pos, cell);
    }

    public boolean set(Position pos, Ship ship) {
        Player p = ship.getOwner();
        p.Ships.put(ship, pos);
        Playground.put(pos, ship);
        return true;
    }

    public int getSize() {
        return size;
    }

    public int getRockCount() {
        return Rocks.length;
    }

    public void prettyPrint(PrintStream ps) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size; ++i) sb.append("+---");
        sb.append("+");
        String line = sb.toString();
        ps.println(line);
        for (int i = 1; i <= size; ++i) {
            for (int j = 1; j <= size; ++j) {
                ICell c = Playground.getOrDefault(Position.get(i, j), BLANK_CELL);
                ps.printf("|%3s", c.toPrettyString());
            }
            ps.println("|");
            ps.println(line);
        }
    }

    @Override
    public void onShipDestroy(Ship ship) {
        for (Position pos : Playground.keySet()) {
            if (Playground.get(pos).equals(ship)) {
                Playground.put(pos, BLANK_CELL);
                return;
            }
        }
    }

    public interface ICell extends java.io.Serializable {
        String toPrettyString();
    }

    public static class BlankCell implements ICell {
        private BlankCell() {
        }

        @Override
        public String toPrettyString() {
            return "   ";
        }
    }

    public static class Rock implements ICell {
        private Rock() {
        }

        @Override
        public String toPrettyString() {
            return " # ";
        }
    }
}
