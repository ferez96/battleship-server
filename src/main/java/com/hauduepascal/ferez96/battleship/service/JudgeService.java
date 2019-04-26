package com.hauduepascal.ferez96.battleship.service;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.controller.Ship;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.handler.AuctionHandler;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;
import org.apache.commons.io.FileUtils;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class JudgeService extends Server implements IService {
    private static final Logger Log = LoggerFactory.getLogger(JudgeService.class);

    public JudgeService(int port) {
        super(port);
    }

    public static void main(String[] args) {
        Log.info("Start JudgeService");
        ConfigurationProvider provider = new ConfigurationProviderBuilder()
                .withConfigurationSource(new FilesConfigurationSource(() ->
                        Collections.singletonList(Global.PROJECT_PATH.resolve("application.yaml"))))
                .build();
        IService server = new JudgeService(provider.getProperty("judge.port", Integer.class));
        server.setupAndStart();
    }

    @Override
    public boolean setupAndStart() {
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(AuctionHandler.class, "/auction");

        this.setHandler(handler);
        try {
            start();
        } catch (Exception e) {
            Log.error("Can not start JudgeService", e);
            return false;
        }
        return true;
    }

    /**
     * Import team <code>name</code> as <code>color</code> player
     *
     * @param name  name of the team will join the match
     * @param color team color
     * @return the Player Object contain core information, return null if can not import the player
     */
    private static Player importPlayer(String name, TeamColor color) {
        Player p = null;
        try {
            Path source = Global.PLAYER_DIR.resolve(name);
            Path target = Global.FIELD_PATH.resolve(color.toString());

            PlayerValidator.checkPlayerDir(source);
            FileUtils.deleteDirectory(target.toFile()); // remove old directory
            FileUtils.copyDirectory(source.toFile(), target.toFile()); // copy
            p = new Player(name, color);
            // compile src
            Utils.compileCpp(p, "SET");
            Utils.compileCpp(p, "PLAY");
        } catch (Exception e) {
            Log.error("Can not load player: " + name, e);
        }
        return p;
    }

    private static void clear() {
        int attempt = 1;
        while (attempt <= 3) {
            try {
                FileUtils.deleteDirectory(Global.FIELD_PATH.toFile());
                return;
            } catch (IOException e) {
                Log.warn("Delete attempt " + attempt + " failed", e);
                attempt--;
            }
        }
    }

    private static int prepare(Map<String, String> params) {
        try {
            Files.createDirectories(Global.FIELD_PATH);
        } catch (IOException e) {
            Log.error("Can not init battle directories", e);
            System.exit(1);
        }

        // Sample not prepare map
        /*// prepare maps
        boolean createMap = Maps.getOrDefault(params, "create-map", true);
        if (createMap)
            try (PrintStream ps = new PrintStream(Global.FIELD_PATH.resolve("map.txt").toFile())) {
                int mapSize = Maps.getOrDefault(params, "size", 8);
                int nRock = Maps.getOrDefault(params, "rocks", 0);
                Playground pg = new Playground(mapSize, nRock);
                ps.println(pg.getSize() + " " + pg.getRockCount());
                for (int i = 1; i <= mapSize; ++i) {
                    for (int j = 1; j <= mapSize; ++j)
                        if (pg.get(Position.get(i, j)) instanceof Playground.Rock) ps.print("#");
                        else ps.print(".");
                    ps.print("\n");
                }

                System.out.println("Generated grid map:");
                pg.prettyPrint(System.out);
            } catch (FileNotFoundException e) {
                Log.error("Can not write map.txt", e);
                System.exit(1);
            }*/

        // prepare ships
        try (PrintStream ps = new PrintStream(Global.FIELD_PATH.resolve("ships.txt").toFile())) {
            int nShip = 5; // 5 ships
            Ship[] ships = new Ship[nShip];
            for (int i = 0; i < nShip; ++i) ships[i] = new Ship();
            for (int i = 0; i < nShip; ++i) ps.println(ships[i]);

            System.out.println("Generated ships:");
            for (int i = 0; i < nShip; ++i) System.out.println(ships[i].toBeautifulString());
        } catch (FileNotFoundException e) {
            Log.error("Can not write ships.txt", e);
            System.exit(1);
        }

        return 0;
    }
}
