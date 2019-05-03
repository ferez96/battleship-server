package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Position;
import com.hauduepascal.ferez96.battleship.controller.Ship;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Normal extends Fire {
    private static final Pattern PATTERN = Pattern.compile("1\\s3(\\s([1-9]|[1-4][0-9]|50)){4}$");

    private Position pos;
    private Position target;
    private Ship ship;
    private int status;

    Normal(String cmd) {
        super(cmd);
        Scanner sc = new Scanner(cmd);
        sc.skip("1\\s3\\s");
        pos = Position.get(sc.nextInt(), sc.nextInt());
        target = Position.get(sc.nextInt(), sc.nextInt());
        ship = null;
        status = 0;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position target) {
        this.target = target;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void check() {
        this.validCommand = ship != null && pos != null && target != null;
    }

    public void success(){status = 1;}

    static public boolean match(String cmd) {
        return PATTERN.matcher(cmd).matches();
    }

    @Override
    public String plain() {
        return String.format("1 3 %d %d %d %d %d",pos.x,pos.y,target.x, target.y, status);
    }
}
