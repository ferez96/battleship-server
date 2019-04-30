package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.cmd.BaseCommand;
import com.hauduepascal.ferez96.battleship.controller.cmd.Fire;
import com.hauduepascal.ferez96.battleship.controller.cmd.Move;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {

    private static class MiniIdZen {
        static private long cnt = 0;

        static long nextId() {
            return cnt++;
        }
    }

    private static final Logger Log = LoggerFactory.getLogger(Player.class);
    public final String Name;
    public final long Id;
    public final TeamColor Color;
    public final Path RootDir;
    private final List<Ship> ships = new ArrayList<>();

    public Player(String name, TeamColor color) throws Exception {
        this(name, color, true);
    }

    public Player(String name, TeamColor color, boolean check) throws Exception {
        this.Id = MiniIdZen.nextId();
        this.Name = name;
        this.Color = color;
        this.RootDir = Global.PLAYER_DIR.resolve(name);
        if (check) PlayerValidator.checkPlayerDir(RootDir);
    }

    boolean addShip(Ship ship) {
        if (ship.setOwner(this)) return ships.add(ship);
        else return false;
    }

    Ship getShip(int i) {
        return ships.get(i);
    }

    public void move(BaseCommand cmd, Playground pg) {
        if (cmd instanceof Move) {
            Move rc = (Move) cmd;
            Optional<Ship> op = ships.stream().filter(x -> x.pos.equals(rc.pos)).findAny();
            if (op.isPresent()) {
                Ship ship = op.get();
                Position nextPos = rc.getNextPosition();
                Playground.ICell nextCell = pg.get(nextPos);
                if (nextCell == null) {
                    Log.warn("Invalid command: new position out if index pos=" + nextPos);
                    rc.validCommand = false;
                } else if (nextCell instanceof Ship) {
                    Log.info("Crash with another ship");
                    ((Ship) nextCell).destroy();
                    ship.destroy();
                } else if (nextCell instanceof Playground.BlankCell) {
                    Log.info("Move ship from " + rc.pos + " to " + nextPos);
                    pg.set(rc.pos, Playground.BLANK_CELL);
                    pg.set(nextPos, ship);
                    ship.pos = nextPos;
                } else {
                    Log.error("Unhandled");
                    rc.validCommand = false;
                }
            } else {
                Log.warn("Invalid command: ship not found, pos=" + rc.pos);
                rc.validCommand = false;
            }
        }
    }

    public void fire(BaseCommand cmd, Playground pg) {
        if (cmd instanceof Fire) {
            Fire fc = (Fire) cmd;
            Optional<Ship> op = ships.stream().filter(x -> x.pos.equals(fc.src)).findAny();
            if (op.isPresent()) {
                Ship ship = op.get();
                int range = ship.range;
                Playground.ICell nextCell = pg.get(fc.target);
                if (Utils.manhattanDistance(fc.src, fc.target) > range) {
                    Log.warn("Invalid command: Out of range " + cmd.plain());
                    fc.validCommand = false;
                } else if (nextCell == null) {
                    Log.warn("Bad command: Fire to nowhere " + cmd.plain());
                    fc.validCommand = false;
                } else if (nextCell instanceof Ship) {
                    int damage = (int) Math.ceil(1.0 * ship.hp * ship.atk / 10);
                    Log.info(ship.toMapInpString() + " attack " + damage + " to " + fc.target);
                    ((Ship) nextCell).takeDamage(damage);
                } else if (nextCell instanceof Playground.BlankCell) {
                    Log.info("Fire to blank cell " + cmd.plain());
                } else {
                    Log.error("Unhandled " + cmd.plain());
                    fc.validCommand = false;
                }
            } else {
                Log.warn("Invalid command: ship not found, pos=" + fc.src);
                fc.validCommand = false;
            }
        }
    }


    public int getAliveShipsCount() {
        int cnt = 0;
        for (Ship ship : ships) {
            if (ship.hp > 0) cnt++;
        }
        return cnt;
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
