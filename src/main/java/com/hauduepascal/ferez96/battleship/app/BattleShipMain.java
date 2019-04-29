package com.hauduepascal.ferez96.battleship.app;


import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.*;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.service.JudgeService;
import com.hauduepascal.ferez96.battleship.service.WebServer;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;
import org.apache.commons.io.FileUtils;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.consul.ConsulConfigurationSourceBuilder;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.context.filesprovider.DefaultConfigFilesProvider;
import org.cfg4j.source.context.propertiesprovider.PropertiesProviderSelector;
import org.cfg4j.source.context.propertiesprovider.YamlBasedPropertiesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class BattleShipMain {
    private static final Logger Log = LoggerFactory.getLogger(BattleShipMain.class);
    static ConfigurationSource cfgSrc;
    public static ConfigurationProvider cfgProvider;

    static {
        cfgSrc = new FilesConfigurationSource(() -> Arrays.asList(Global.PROJECT_PATH.resolve("application.yaml")));
        cfgProvider = new ConfigurationProviderBuilder().withConfigurationSource(cfgSrc).build();
    }

    public static void main(String[] args) {
        System.out.println("Author: " + ConfProvider.Instance.getProperty("app.author", String.class));
        new WebServer(cfgProvider.getProperty("webserver.port", Integer.class)).setupAndStart();
        new JudgeService().setupAndStart();
    }
}
