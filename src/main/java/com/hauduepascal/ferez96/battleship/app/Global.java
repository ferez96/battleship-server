package com.hauduepascal.ferez96.battleship.app;

import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * hard code configurations
 */
public class Global {
    public static final Path PROJECT_PATH = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
    public static final ConfigurationProvider Conf = new ConfigurationProviderBuilder()
            .withConfigurationSource(new FilesConfigurationSource(() -> Collections.singleton(PROJECT_PATH.resolve("application.yaml")))).build();
    public static final Path FIELD_PATH = PROJECT_PATH.resolve("BATTLE_FIELD");
    public static final Path DB_HOME = Paths.get(Conf.getProperty("db_home", String.class));
    public static final Path PLAYER_DIR = Paths.get(Conf.getProperty("players.dir", String.class));
    public static final Path TMP_DIR = PROJECT_PATH.resolve(".tmp");
    public static final int N_SHIPS = 5;


}
