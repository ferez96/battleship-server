package com.hauduepascal.ferez96.battleship.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Position {
    public static final Position ZERO = new Position(0, 0);
    private static final Pool POOL = new Pool();
    public final int x;
    public final int y;

    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position get(int x, int y) {
        return POOL.get(x, y);
    }

    private static class Pool {
        private static final Logger Log = LoggerFactory.getLogger(Pool.class);
        private final HashMap<Integer, HashMap<Integer, Position>> POOL = new HashMap<>();
        private long size = 0;

        private boolean contains(int x, int y) {
            if (POOL.containsKey(x)) {
                return POOL.get(x).containsKey(y);
            }
            return false;
        }

        private Position pooling(Position pos) {
            int x = pos.x;
            int y = pos.y;
            Log.trace(String.format("Create new object Position(%d, %d) in pool %s, size=%s", x, y, this.hashCode(), ++this.size));
            HashMap<Integer, Position> row = POOL.getOrDefault(x, new HashMap<>());
            row.put(y, pos);
            POOL.put(x, row);
            return pos;
        }

        private Position get(int x, int y) {
            if (x <= 0 || y <= 0) return null;
            if (contains(x, y)) return POOL.get(x).get(y);
            else return pooling(new Position(x, y));
        }
    }

    @Override
    public String toString() {
        return String.format("(%2d,%2d)", x, y);
    }
}
