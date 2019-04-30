package com.hauduepascal.ferez96.battleship.common;

import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.controller.Position;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    private static final Logger Log = LoggerFactory.getLogger(Utils.class);

    public static Path getFilePath(Path dir, String filename) {
        try {
            List<Path> matchPaths = Files.list(dir)
                    .filter(x -> x.getFileName().toString().equalsIgnoreCase(filename))
                    .collect(Collectors.toList());
            if (matchPaths.size() >= 1) {
                return matchPaths.get(0);
            } else {
                throw new IOException(filename + " is not found");
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static int manhattanDistance(Position A, Position B) {
        return Math.abs(A.x - B.x) + Math.abs(A.y - B.y);
    }


    public static int compileCpp(Path source, Path dir) throws IOException {
        String filename = source.getFileName().toString().replace(".CPP", "");

        Path logfile = dir.resolve("log").resolve(filename + ".compile.log").toAbsolutePath();
        Path outfile = dir.resolve("bin").resolve(filename + ".EXE").toAbsolutePath();
        Files.createDirectories(logfile.getParent());
        Files.createDirectories(outfile.getParent());
        Map map = new HashMap();
        map.put("srcfile", source.toAbsolutePath());
        map.put("outfile", outfile);
//        map.put("logfile", logfile);

        CommandLine cmd = new CommandLine("g++");
        cmd.setSubstitutionMap(map);
        cmd.addArgument("-o2", false);
        cmd.addArgument("-std=c++14", false);
        cmd.addArgument("-o", false);
        cmd.addArgument("${outfile}", true);
        cmd.addArgument("${srcfile}", true);

        Log.info("Execute Command: " + cmd);

        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog wd = new ExecuteWatchdog(60000);
        executor.setWatchdog(wd);
        executor.setStreamHandler(new PumpStreamHandler(new FileOutputStream(logfile.toFile())));
        return executor.execute(cmd);
    }
}