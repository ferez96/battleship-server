package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private static class MiniIdZen {
        static private long cnt = 0;

        static long nextId() {
            return cnt++;
        }
    }

    public final String Name;
    public final long Id;
    public final TeamColor Color;
    public final Path RootDir;
    private final List<Ship> ships = new ArrayList<>();

    public Player(String name, TeamColor color) throws Exception {
        this(name, color, false);
    }

    public Player(String name, TeamColor color, boolean checked) throws Exception {
        this.Id = MiniIdZen.nextId();
        this.Name = name;
        this.Color = color;
        this.RootDir = Global.FIELD_PATH.resolve(color.toString().toLowerCase()).toAbsolutePath();
        if (!checked) PlayerValidator.checkPlayerDir(RootDir);
    }

    public boolean addShip(Ship ship) {
        int id = ships.size() + 1;
        Ship myShip = new Ship(id, ship.getHp(), ship.atk, ship.range);
        return ships.add(myShip);
    }

    public Ship getShip(int i) {
        return ships.get(i);
    }

    public int getShipsCount() {
        return ships.size();
    }

    @Override
    public String toString() {
        String sb = "==== " + this.Color + " ====" + "\n" +
                "Id:  \t" + this.Id + "\n" +
                "Name:\t" + this.Name + "\n" +
                "Dir: \t" + this.RootDir + "\n";
        return sb;
    }
}
