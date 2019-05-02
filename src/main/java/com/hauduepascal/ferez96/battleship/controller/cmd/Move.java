package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Position;
import com.hauduepascal.ferez96.battleship.controller.Ship;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Move extends BaseCommand {
    private Position pos = null;
    private Ship ship = null;
    private String s = null;
    private static final Pattern PATTERN = Pattern.compile("0 ([1-9]|[1-4][0-9]|50) ([1-9]|[1-4][0-9]|50) [UDLR]+");


    public static Position getNextPosition(Position pos, char h) {
        if (h == 'U') return Position.get(pos.x - 1, pos.y);
        if (h == 'D') return Position.get(pos.x + 1, pos.y);
        if (h == 'L') return Position.get(pos.x, pos.y - 1);
        if (h == 'R') return Position.get(pos.x, pos.y + 1);
        return null;
    }

    static public boolean match(String cmd) {
        return PATTERN.matcher(cmd).matches();
    }

    Move(String cmd) {
        Scanner sc = new Scanner(cmd);
        this.code = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        this.pos = Position.get(x, y);
        this.s = sc.next();
    }

    public String getS() {
        return s;
    }

    public Position getPos() {
        return pos;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public void check() {
        validCommand = pos != null && ship != null && s != null;
    }

    public ArrayList<Position> getMovePath() {
        Move move = this;
        ArrayList<Position> path = new ArrayList<>();
        Position prev;
        prev = move.getPos();
        for (char h : move.getS().toCharArray()) {
            prev = Move.getNextPosition(prev, h);
            if (prev == null) break;
            else path.add(prev);
        }
        return path;
    }

    @Override
    public String plain() {
        return String.format("%s %s %s %s", code, pos.x, pos.y, s);
    }

    @Override
    public ResultData execute() {
        return null;
    }

}
