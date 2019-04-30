package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.cmd.BaseCommand;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class Judge {
    private static final class MiniIdZen {
        static private long cnt = 0;

        static long nextId() {
            return cnt++;
        }
    }

    private static final Logger Log = LoggerFactory.getLogger(Judge.class);
    private static final ConfigurationProvider Conf = Global.Conf;

    private final long GAME_ID;
    private Player[] p = new Player[2];
    private Playground pg;
    private List<BaseCommand> Report1 = new ArrayList<>();
    private List<BaseCommand> Report2 = new ArrayList<>();
    private Path dir;


    public Judge() {
        this.GAME_ID = MiniIdZen.nextId();
    }

    public void importPlayers(Player p1, Player p2) {
        this.p[0] = p1;
        this.p[1] = p2;
        this.dir = Paths.get(Conf.getProperty("db_home", String.class)).resolve("games").resolve(String.valueOf(GAME_ID));
    }

    public boolean setup() {
        try {
            Files.createDirectories(this.dir);
            Files.createDirectories(this.dir.resolve("black"));
            Files.createDirectories(this.dir.resolve("white"));
            Files.createDirectories(this.dir.resolve("results"));
        } catch (IOException ex) {
            Log.error("Can not create data directories: " + this.dir, ex);
            return false;
        }

        // Check players source codes
        for (Player p : p) {
            Path dir = p.RootDir;
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                Path auction_cpp, set_cpp, play_cpp;
                try {
                    List<Path> files = Files.list(dir).collect(Collectors.toList());
                    Optional<Path> opt_auction_cpp = files.stream().filter((x) -> x.getFileName().toString().equals("AUCTION.CPP")).findAny();
                    Optional<Path> opt_set_cpp = files.stream().filter((x) -> x.getFileName().toString().equals("SET.CPP")).findAny();
                    Optional<Path> opt_play_cpp = files.stream().filter((x) -> x.getFileName().toString().equals("PLAY.CPP")).findAny();

                    if ((opt_auction_cpp.isPresent() && opt_set_cpp.isPresent() && opt_play_cpp.isPresent())) {
                        auction_cpp = opt_auction_cpp.get();
                        set_cpp = opt_set_cpp.get();
                        play_cpp = opt_play_cpp.get();
                    } else return false;
                } catch (IOException ex) {
                    return false;
                }

                // compile source codes
                for (Path src : new Path[]{auction_cpp, set_cpp, play_cpp}) {
                    try {
                        Log.info("Compile source file: " + src);
                        int rv = Utils.compileCpp(src, this.dir.resolve(p.Color.toString()));
                        if (rv != 0) {
                            Log.warn("Compile file " + src + " error, return: " + rv);
                            return false;
                        }
                    } catch (IOException ex) {
                        Log.warn("Compile file " + src + " fail", ex);
                        return false;
                    }
                }

            } else return false;
        }


        // Export properties
        try (FileOutputStream fos = new FileOutputStream(this.dir.resolve("info.properties").toFile())) {
            Properties prop = new Properties();
            prop.setProperty("game_id", "" + GAME_ID);
            for (Player player : p) prop.setProperty("player." + player.Color, String.valueOf(player.Id));
            prop.store(fos, "Created by HauDuePascal-JudgeSystem");
        } catch (IOException ex) {
            Log.error("Can not print info", ex);
            return false;
        }

        return true;
    }

    public void phase1() {
        Log.info("Start phase 1");
    }

    public void phase2() {
        Log.info("Start phase 2");
    }

    public void phase3() {
        Log.info("Start phase 3");
    }

    private void writeReport() {
    }
}
