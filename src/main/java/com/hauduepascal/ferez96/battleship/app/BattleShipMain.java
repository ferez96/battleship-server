package com.hauduepascal.ferez96.battleship.app;


import com.hauduepascal.ferez96.battleship.controller.*;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class BattleShipMain {
    private static final Logger Log = LoggerFactory.getLogger(BattleShipMain.class);
    static ConfigurationSource cfgSrc;
    public static ConfigurationProvider cfgProvider;

    static {
        cfgSrc = new FilesConfigurationSource(() -> Arrays.asList(Global.PROJECT_PATH.resolve("application.yaml")));
        cfgProvider = new ConfigurationProviderBuilder().withConfigurationSource(cfgSrc).build();
    }

    public static void main(String[] args) {
//        System.out.println("Author: " + ConfProvider.Instance.getProperty("app.author", String.class));
//        new WebServer(cfgProvider.getProperty("webserver.port", Integer.class)).setupAndStart();
//        new JudgeService().setupAndStart();

        Judge judge = new Judge();
        try {
            judge.importPlayers(new Player("AI1", TeamColor.BLACK), new Player("AI2", TeamColor.WHITE));
            if (judge.setup()) {
                judge.phase1();
                judge.phase2();
                judge.phase3();
            }
        } catch (Exception e) {
            Log.info("Nah", e);
        }

    }
}
