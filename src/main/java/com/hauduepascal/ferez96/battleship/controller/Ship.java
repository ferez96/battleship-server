package com.hauduepascal.ferez96.battleship.controller;

import java.util.Random;

public class Ship implements Playground.ICell {
    private static final Random RANDOM = new Random();
    private static final char SID[] = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    public final int id;
    private int hp;
    public final int atk;
    public final int range;

    static private int rand(int s, int t) {
        return RANDOM.nextInt(t - s + 1) + s;
    }

    Ship(int id, int hp, int atk, int range) {
        this.id = id;
        this.hp = hp;
        this.atk = atk;
        this.range = range;
    }

    public Ship() {
        this(-1, rand(5, 10), rand(1, 7), rand(1, 7));
    }

    public int getHp() {
        return hp;
    }

    @Override
    public String toString() {
        return this.hp + " " + this.atk + " " + this.range;
    }

    public String toBeautifulString() {
        return String.format("<HP:%2d, ATK:%2d, RANGE:%2d>", this.hp, this.atk, this.range);
    }

    @Override
    public String toPrettyString() {
        if (id == -1) return null;
        return String.format("%s%02d", SID[id], hp);
    }
}
