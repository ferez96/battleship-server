package com.hauduepascal.ferez96.battleship.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


/**
 * Self pooling Position class.
 * Accept: 1 <= x <= 50, 1 <= y <= 50
 *
 * @author ferez
 */
public class Position {
    private static class PosPool {
        private static final Logger Log = LoggerFactory.getLogger(Position.PosPool.class);
        private final HashMap<Short, HashMap<Short, Position>> POOL = new HashMap<>();
        private long size = 0;

        private boolean contains(int x, int y) {
            if (POOL.containsKey((short) x)) {
                return POOL.get((short) x).containsKey((short) y);
            }
            return false;
        }

        private Position pooling(Position pos) {
            this.size++;
            Log.trace(String.format("Create new object Position(%d, %d) in pool %s, size=%s", pos.x, pos.y, this.hashCode(), size));
            HashMap<Short, Position> row = POOL.getOrDefault(pos.x, new HashMap<>());
            row.put(pos.y, pos);
            POOL.put(pos.x, row);
            return pos;
        }

        private Position get(int x, int y) {
            if (x <= 0 || y <= 0 || x > 50 || y > 50) return null;
            if (contains(x, y)) return POOL.get((short) x).get((short) y);
            else return pooling(new Position((short) x, (short) y));
        }
    }

    static private final PosPool Pool = new PosPool();
    static public final Position ZERO = new Position((short) 0, (short) 0); // Special (0,0) position
    public final short x;
    public final short y;

    private Position(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public static Position get(int x, int y) {
        return Pool.get(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;

    }

    @Override
    public int hashCode() {
        int result = (int) x;
        result = 31 * result + (int) y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
