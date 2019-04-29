package com.hauduepascal.ferez96.battleship.app;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class ConfProvider {
    public static final ConfigurationProvider Instance = new ConfigurationProviderBuilder().withConfigurationSource(new FilesConfigurationSource(() -> Collections.singletonList(Paths.get(System.getProperty("user.dir")).resolve("application.yaml")))).build();

    public static ConfigurationProvider get(Path configPath) {
        return new ConfigurationProviderBuilder().withConfigurationSource(new FilesConfigurationSource(() -> Collections.singletonList(configPath))).build();
    }
}
