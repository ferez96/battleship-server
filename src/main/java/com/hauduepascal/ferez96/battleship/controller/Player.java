package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.controller.cmd.*;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Player implements ShipDestroyListener {

    @Override
    public void onShipDestroy(Ship ship) {
        this.Ships.put(ship, Position.ZERO);
    }

    static enum playStatus {Playing, Waiting, Eliminated}

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
    private playStatus status;

    // game info
    final Map<Ship, Position> Ships = new LinkedHashMap<>();
    final List<ICommand> History = new ArrayList<>();
    Path playDir;
    private int nRocket;

    public Player(String name, TeamColor color) throws Exception {
        this(name, color, true);
    }

    public Player(String name, TeamColor color, boolean check) throws Exception {
        this.Id = MiniIdZen.nextId();
        this.Name = name;
        this.Color = color;
        this.RootDir = Global.PLAYER_DIR.resolve(name);
        this.status = playStatus.Waiting;
        if (check) PlayerValidator.checkPlayerDir(RootDir);
    }

    boolean prepare(Path playDir) {
        if (this.status != playStatus.Waiting) return false;
        this.playDir = playDir;
        this.status = playStatus.Playing;
        this.Ships.clear();
        this.History.clear();
        this.nRocket = 3;
        return true;
    }

    private Ship getShipAt(Position pos) {
        for (Ship ship : Ships.keySet()) {
            Position sPos = Ships.get(ship);
            if (sPos.equals(pos)) return ship;
        }
        return null;
    }

    public void prepare(ICommand command) {
        if (command == null) {
            History.add(ICommand.NULL_CMD);
        } else {
            if (command instanceof Move) {
                Move cmd = (Move) command;
                Position pos = cmd.getPos();
                for (Ship ship : Ships.keySet()) {
                    Position sPos = Ships.get(ship);
                    if (sPos.equals(pos)) {
                        cmd.setShip(ship);
                        break;
                    }
                }
                cmd.check();
            } else if (command instanceof Normal) {
                Normal cmd = (Normal) command;
                Position pos = cmd.getPos();
                Ship ship = getShipAt(pos);
                cmd.setShip(ship);
                cmd.check();
            } else if (command instanceof Flare){
                Flare cmd = (Flare) command;
                cmd.setPlayer(this);
                cmd.setCount(0);
                cmd.check();
            } else if (command instanceof Rocket){
                Rocket cmd = (Rocket) command;
                if (nRocket>0){
                    nRocket--;
                    cmd.setPlayer(this);
                }else{
                    Log.trace("Run out of rockets");
                }
                cmd.check();
            }
            History.add(command);
        }
    }

    public int getAliveShipsCount() {
        return (int) Ships.keySet().stream().filter(x -> x.hp > 0).count();
    }

    public int getShipsCount() {
        return Ships.size();
    }

    @Override
    public String toString() {
        return "==== " + this.Color + " ====" + "\n" +
                "Id:  \t" + this.Id + "\n" +
                "Name:\t" + this.Name + "\n" +
                "Dir: \t" + this.RootDir + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (Id != player.Id) return false;
        if (!Name.equals(player.Name)) return false;
        if (Color != player.Color) return false;
        return RootDir.equals(player.RootDir);

    }

    @Override
    public int hashCode() {
        int result = Name.hashCode();
        result = 31 * result + Color.hashCode();
        result = 31 * result + RootDir.hashCode();
        return result;
    }
}
