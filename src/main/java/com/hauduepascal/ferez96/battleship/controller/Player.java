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

    public final String Name;
    public final long Id;
    public final TeamColor Color;
    public final Path RootDir;

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

    @Override
    public String toString() {
        String sb = "==== " + this.Color + " ====" + "\n" +
                "Id:  \t" + this.Id + "\n" +
                "Name:\t" + this.Name + "\n" +
                "Dir: \t" + this.RootDir + "\n";
        return sb;
    }
}
