package com.hauduepascal.ferez96.battleship.app;

import com.hauduepascal.ferez96.battleship.enums.TeamColor;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * hard code configurations
 */
public class Global {
    public static final Path PROJECT_PATH = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
    public static final Path FIELD_PATH = PROJECT_PATH.resolve("BATTLE_FIELD");
    public static final Path BLACK_PATH = PROJECT_PATH.resolve(TeamColor.Black.toString());
    public static final Path WHITE_PATH = PROJECT_PATH.resolve(TeamColor.White.toString());
    public static final Path PLAYER_DIR = PROJECT_PATH.resolve("player");
    public static final Path TMP_DIR = PROJECT_PATH.resolve(".tmp");
    public static final int N_SHIPS = 5;
}
