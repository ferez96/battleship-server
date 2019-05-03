package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.controller.Playground;
import com.hauduepascal.ferez96.battleship.controller.Position;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Flare extends Fire {
    private static final Pattern PATTERN = Pattern.compile("1\\s1(\\s([1-9]|[1-4][0-9]|50)){4}$");

    private Position top_left;
    private Position bottom_right;
    private int count;
    private Player player;

    Flare(String cmd) {
        super(cmd);
        Scanner sc = new Scanner(cmd);
        sc.skip("1\\s1\\s");
        top_left = Position.get(sc.nextInt(), sc.nextInt());
        bottom_right = Position.get(sc.nextInt(), sc.nextInt());
        count = -1;
        player = null;
    }

    public Position getTop_left() {
        return top_left;
    }

    public Position getBottom_right() {
        return bottom_right;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void check() {
        validCommand = count != -1 && player != null && top_left.x <= bottom_right.x && top_left.y <= bottom_right.y;
    }

    static public boolean match(String cmd) {
        return PATTERN.matcher(cmd).matches();
    }

    @Override
    public String plain() {
        return null;
    }
}
