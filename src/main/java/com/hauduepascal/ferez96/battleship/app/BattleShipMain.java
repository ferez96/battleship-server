package com.hauduepascal.ferez96.battleship.app;


import com.hauduepascal.ferez96.battleship.controller.Judge;
import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import org.apache.commons.io.FileUtils;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class BattleShipMain {
    private static final Logger Log = LoggerFactory.getLogger(BattleShipMain.class);
    static ConfigurationSource cfgSrc;
    public static ConfigurationProvider cfgProvider;

    static {
        cfgSrc = new FilesConfigurationSource(() -> Arrays.asList(Global.PROJECT_PATH.resolve("application.yaml")));
        cfgProvider = new ConfigurationProviderBuilder().withConfigurationSource(cfgSrc).build();
    }

    public static void main(String[] args) throws Exception {
//        System.out.println("Author: " + ConfProvider.Instance.getProperty("app.author", String.class));
//        new WebServer(cfgProvider.getProperty("webserver.port", Integer.class)).setupAndStart();
//        new JudgeService().setupAndStart();
        FileUtils.deleteDirectory(Global.DB_HOME.toFile());
        Log.info("============ Start new session ============\n\n");
        Judge judge = new Judge();

        judge.importPlayers(new Player("AI1", TeamColor.BLACK, false), new Player("AI2", TeamColor.WHITE, false));
        if (judge.setup()) {
            Field dir = Judge.class.getDeclaredField("dir");
            dir.setAccessible(true);
            Files.copy(Global.PROJECT_PATH.resolve("map_pool").resolve("map0"), ((Path) dir.get(judge)).resolve("MAP"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Global.PROJECT_PATH.resolve("map_pool").resolve("ship0"), ((Path) dir.get(judge)).resolve("SHIPS"), StandardCopyOption.REPLACE_EXISTING);
            judge.phase1();
            judge.phase2();
            judge.phase3();
        }

    }
}
