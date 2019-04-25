package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.controller.Playground.ICell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ship implements ICell {

    private static final Random RANDOM = new Random();
    private static final char[] SID = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private static final Logger Log = LoggerFactory.getLogger(Ship.class);
    private List<ShipDestroyListener> shipDestroyListeners = new ArrayList<>();

    final int id;
    final Player owner;
    int hp;
    final int atk;
    final int range;
    Position pos = Position.ZERO;
    int status = 0;

    static private int rand(int s, int t) {
        return RANDOM.nextInt(t - s + 1) + s;
    }

    Ship(int id, int hp, int atk, int range, Player owner) {
        this.id = id;
        this.hp = hp;
        this.atk = atk;
        this.range = range;
        this.owner = owner;
    }

    public Ship() {
        this(-1, rand(5, 10), rand(1, 7), rand(5, 10), null);
    }

    public void addShipDestroyListener(ShipDestroyListener listener) {
        shipDestroyListeners.add(listener);
    }

    void destroy() {
        Log.info("Ship " + toMapInpString() + " has been destroyed");
        shipDestroyListeners.forEach(x -> x.onShipDestroy(this));
        hp = 0;
        pos = Position.ZERO;
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
        return String.format("%s%02d", owner.Color.toString().toUpperCase().charAt(0), hp);
    }

    public String toMapInpString() {
        return String.format("%d %d %d %d %d %d", hp, atk, range, pos.x, pos.y, status);
    }

    public void takeDamage(int d) {
        if (d >= hp) destroy();
        else hp -= d;
        status = 1;
    }
}

interface ShipDestroyListener {
    void onShipDestroy(Ship ship);
}