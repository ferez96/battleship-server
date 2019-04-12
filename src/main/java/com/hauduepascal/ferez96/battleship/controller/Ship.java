package com.hauduepascal.ferez96.battleship.controller;

import java.util.Random;

public class Ship {
    private static final Random RANDOM = new Random();

    private int hp;
    private int atk;
    private int range;

    protected Ship(int hp, int atk, int range) {
        this.hp = hp;
        this.atk = atk;
        this.range = range;
    }

    public Ship() {
        this(RANDOM.nextInt(6) + 5, RANDOM.nextInt(7) + 1, RANDOM.nextInt(7) + 1); // [5,1,1] to [10,7,7]
    }

    @Override
    public String toString() {
        String sb = "[" +
                "HP:" + this.hp + "|" +
                "ATK:" + this.atk + "|" +
                "RANGE:" + this.range + "]";
        return sb;
    }
}
