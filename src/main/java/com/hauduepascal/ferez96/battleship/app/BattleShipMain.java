package com.hauduepascal.ferez96.battleship.app;


import com.hauduepascal.ferez96.battleship.controller.Judge;
import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import org.apache.commons.io.FileUtils;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class BattleShipMain {
    private static final Logger Log = LoggerFactory.getLogger(BattleShipMain.class);
    private static ConfigurationProvider cfg;

    public static void main(String[] args) throws Exception {
        cfg = new ConfigurationProviderBuilder().withConfigurationSource(
                new FilesConfigurationSource(() -> Collections.singleton(Global.PROJECT_PATH.resolve("play.yaml")))).build();
        Match m = cfg.bind("", Match.class);
        FileUtils.deleteDirectory(Global.DB_HOME.toFile());
        Log.info("============ Start new session ============\n\n");

        Judge judge = new Judge();

        judge.importPlayers(
                new Player(m.black(), TeamColor.BLACK, true),
                new Player(m.white(), TeamColor.WHITE, true));
        if (judge.setup()) {
            Field dir = Judge.class.getDeclaredField("dir");
            dir.setAccessible(true);
            Files.copy(Global.PROJECT_PATH.resolve("map_pool").resolve(m.map()),
                    ((Path) dir.get(judge)).resolve("MAP"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Global.PROJECT_PATH.resolve("map_pool").resolve(m.ship()),
                    ((Path) dir.get(judge)).resolve("SHIPS"), StandardCopyOption.REPLACE_EXISTING);

            judge.phase1();
            judge.phase2();
            judge.phase3();
        }

    }
}
