package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.enums.TeamColor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private static class MiniIdZen {
        static private long cnt = 0;

        static long nextId() {
            return cnt++;
        }
    }

    final private String name;
    final private long id;
    final private TeamColor color;
    final private Path rootDir;

    public TeamColor getColor() {
        return color;
    }

    public Path getRootDir() {
        return rootDir;
    }

    public Player(String name, TeamColor color) throws Exception {
        this.id = MiniIdZen.nextId();
        this.name = name;
        this.color = color;
        this.rootDir = Paths.get(System.getProperty("user.dir"))
                .resolve("battleField")
                .resolve(color.toString().toLowerCase());
        checkPlayerDir(rootDir);
    }

    private static void checkPlayerDir(Path dir) throws Exception {
        if (!Files.isDirectory(dir)) throw new Exception(dir + " is not a directory.");
        List<String> sourceFiles = Files.list(dir)
                .map(p -> p.getFileName().toString())
                .filter(name -> name.toUpperCase().endsWith(".CPP"))
                .collect(Collectors.toList());
        if (sourceFiles.stream().noneMatch(s -> s.toUpperCase().equals("SET.CPP")))
            throw new Exception("SET.CPP is not found");
        if (sourceFiles.stream().noneMatch(s -> s.toUpperCase().equals("PLAY.CPP")))
            throw new Exception("PLAY.CPP is not found");
    }

    @Override
    public String toString() {
        String sb = "==== " + this.color + " ====" + "\n" +
                "Id:  \t" + this.id + "\n" +
                "Name:\t" + this.name + "\n" +
                "Dir: \t" + this.rootDir + "\n";
        return sb;
    }
}
