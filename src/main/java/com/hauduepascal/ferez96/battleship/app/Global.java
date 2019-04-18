package com.hauduepascal.ferez96.battleship.app;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * hard code configurations
 */
public class Global {
    public static final Path PROJECT_PATH = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
    public static final Path FIELD_PATH = PROJECT_PATH.resolve("battleField");
}
