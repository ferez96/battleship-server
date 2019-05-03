package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Player;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Rocket extends Fire {
    private static final Pattern PATTERN = Pattern.compile("1\\s2\\s[12]\\s([1-9]|[1-4][0-9]|50)$");

    private int index;
    private int direction;
    private Player player;

    Rocket(String cmd) {
        super(cmd);
        Scanner sc = new Scanner(cmd);
        sc.skip("1\\s2\\s");
        direction = sc.nextInt();
        index = sc.nextInt();
        player = null;
    }

    public int getIndex() {
        return index;
    }

    public int getDirection() {
        return direction;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void check(){
        validCommand = player!=null;
    }

    static public boolean match(String cmd) {
        return PATTERN.matcher(cmd).matches();
    }

    @Override
    public String plain() {
        return null;
    }
}
