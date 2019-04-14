package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;

import java.nio.file.Path;

public class Player {
    private static class MiniIdZen {
        static private long cnt = 0;

        static long nextId() {
            return cnt++;
        }
    }

    final private String name;
    final private long id;
    final private TeamColor color;
    final private Path rootDir;

    public TeamColor getColor() {
        return color;
    }

    public Path getRootDir() {
        return rootDir;
    }

    public Player(String name, TeamColor color) throws Exception {
        this.id = MiniIdZen.nextId();
        this.name = name;
        this.color = color;
        this.rootDir = Global.FIELD_PATH.resolve(color.toString().toLowerCase());
        PlayerValidator.checkPlayerDir(rootDir);
    }

    @Override
    public String toString() {
        String sb = "==== " + this.color + " ====" + "\n" +
                "Id:  \t" + this.id + "\n" +
                "Name:\t" + this.name + "\n" +
                "Dir: \t" + this.rootDir + "\n";
        return sb;
    }
}
