package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.controller.Playground.ICell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ship implements ICell, Comparable<Ship> {

    private static class CONST {
        static final int TAKE_DAMAGE = 1;
        static final int NOT_TAKE_DAMAGE = 0;
    }

    private static final Random RANDOM = new Random();
    private static final Logger Log = LoggerFactory.getLogger(Ship.class);
    private List<ShipDestroyListener> shipDestroyListeners = new ArrayList<>();

    final long id;
    private Player owner = null;
    int hp;
    int atk;
    int range;
    int move;
    int status = CONST.NOT_TAKE_DAMAGE;

    static private int rand(int s, int t) {
        return RANDOM.nextInt(t - s + 1) + s;
    }

    Ship(long id, int hp, int atk, int range, int move, Player owner) {
        this.id = id;
        this.hp = hp;
        this.atk = atk;
        this.range = range;
        this.move = move;
        this.owner = owner;
        if (owner!=null) addShipDestroyListener(owner);
    }

    @Deprecated
    public Ship() {
        this(-1, rand(5, 20), rand(1, 7), rand(15, 45), rand(1, 5), null);
    }

    public void addShipDestroyListener(ShipDestroyListener listener) {
        shipDestroyListeners.add(listener);
    }

    public int getHp() {
        return hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getRange() {
        return range;
    }

    public int getMove() {
        return move;
    }

    public boolean setOwner(Player owner) {
        if (this.owner != null) return false;
        this.owner = owner;
        addShipDestroyListener(owner);
        return true;
    }

    public Player getOwner() {
        return owner;
    }

    void destroy() {
        Log.trace("Ship " + this + " has been destroyed");
        hp = 0;
        shipDestroyListeners.forEach(x -> x.onShipDestroy(this));
    }

    void renewStatus() {
        status = CONST.NOT_TAKE_DAMAGE;
    }

    void takeDamage(int d) {
        takeDamage(d, false);
    }

    void takeDamage(int d, boolean beFired) {
        if (beFired) status = CONST.TAKE_DAMAGE;
        Log.trace("Ship " + this + " take " + d + " damage");
        if (d >= hp) destroy();
        else hp -= d;
    }

    @Override
    public String toString() {
        return String.format("<%05d>{HP:%2d, ATK:%d, RANGE:%2d, MOVE:%d | owner:%s, status:%d}", id, hp, atk, range, move, owner.Color, status);
    }

    @Override
    public String toPrettyString() {
        if (id == -1) return null;
        return String.format("%s%02d", owner.Color.toString().toUpperCase().charAt(0), hp);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        if (id != ship.id) return false;
        if (hp != ship.hp) return false;
        if (atk != ship.atk) return false;
        if (range != ship.range) return false;
        if (move != ship.move) return false;
        return owner != null ? owner.equals(ship.owner) : ship.owner == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Ship o) {
        return hp != o.hp ? hp - o.hp :
                atk != o.atk ? atk - o.atk :
                        range != o.range ? range - o.range :
                                move - o.move;
    }
}

interface ShipDestroyListener {
    void onShipDestroy(Ship ship);
}