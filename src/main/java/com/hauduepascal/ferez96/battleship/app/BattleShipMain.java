package com.hauduepascal.ferez96.battleship.app;


import com.hauduepascal.ferez96.battleship.common.Maps;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.*;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class BattleShipMain {
    private static final Logger Log = LoggerFactory.getLogger(BattleShipMain.class);

    public static void main(String[] args) {
        System.out.println("Vong chung ket cuoc thi Hau Due Pascal");
        System.out.println("He thong cham bai");
        Log.debug("You did call with arguments: " + Arrays.toString(args));
        Map<String, String> params = Utils.parseArgs(args);

        if (args.length > 0) {
            switch (args[0]) {
                case "prepare":
                    int retVal = prepare(params);
                    if (retVal != 0) System.exit(retVal);
                    break;
                case "clear":
                    try {
                        FileUtils.deleteDirectory(Global.FIELD_PATH.toFile());
                    } catch (IOException e) {
                        Log.warn("Delete attempt failed", e);
                    }
                    break;

                case "play":
                    Player w = importPlayer("AI1", TeamColor.White);
                    if (w != null) System.out.println(w);
                    Player b = importPlayer("AI2", TeamColor.Black);
                    if (b != null) System.out.println(b);

                    if (b == null || w == null) System.exit(1);
                    else {
                        Judge judge = new Judge();
                        judge.importPlayers(b, w);
                        judge.phrase1();
                        judge.phrase2();
                        judge.phrase3();
                    }
                    break;
                default:
                    printHelp();
            }
        } else {
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("Help:");
        System.out.println("\tprepare\t-\tChuan bi thuyen va san thi dau");
        System.out.println("\tplay\t-\tBat dau thi dau");
        System.out.println("\tclear\t-\tXoa resource cu");
    }

    /**
     * Import team <code>name</code> as <code>color</code> player
     * @param name name of the team will join the match
     * @param color team color
     * @return the Player Object contain core information
     */
    private static Player importPlayer(String name, TeamColor color) {
        Player p = null;
        try {
            Path source = Global.PROJECT_PATH.resolve("player").resolve(name);
            Path target = Global.FIELD_PATH.resolve(color.toString());
            PlayerValidator.checkPlayerDir(source);
            FileUtils.deleteDirectory(target.toFile());
            FileUtils.copyDirectory(source.toFile(), target.toFile());
            p = new Player(name, color);
        } catch (Exception e) {
            Log.error("Can not load player: " + name, e);
        }
        return p;
    }


    private static int prepare(Map<String, String> params) {
        try {
            Files.createDirectories(Global.FIELD_PATH);
            Files.createDirectories(Global.FIELD_PATH.resolve("black"));
            Files.createDirectories(Global.FIELD_PATH.resolve("white"));
        } catch (IOException e) {
            Log.error("Can not create battle directories", e);
            System.exit(1);
        }

        // prepare ships
        try (PrintStream ps = new PrintStream(Global.FIELD_PATH.resolve("ships.txt").toFile())) {
            int nShip = Maps.getOrDefault(params, "nShip", 10);
            Ship[] ships = new Ship[nShip];
            for (int i = 0; i < nShip; ++i) ships[i] = new Ship();
            for (int i = 0; i < nShip; ++i) ps.println(ships[i]);
        } catch (FileNotFoundException e) {
            Log.error("Can not write ships.txt", e);
            System.exit(1);
        }

        // prepare maps
        boolean createMap = Maps.getOrDefault(params, "create-map", true);
        if (createMap)
            try (PrintStream ps = new PrintStream(Global.FIELD_PATH.resolve("map.txt").toFile())) {
                int mapSize = Maps.getOrDefault(params, "size", 8);
                int nRock = Maps.getOrDefault(params, "rocks", 0);
                Playground pg = new Playground(mapSize, nRock);
                ps.println(pg.getSize() + " " + pg.getRockCount());
                for (int i = 1; i <= mapSize; ++i) {
                    for (int j = 1; j <= mapSize; ++j)
                        if (pg.get(Position.get(i, j)) instanceof Playground.Rock)
                            ps.printf("%d %d\n", i, j);
                }
            } catch (FileNotFoundException e) {
                Log.error("Can not write map.txt", e);
                System.exit(1);
            }

        return 0;
    }
}
