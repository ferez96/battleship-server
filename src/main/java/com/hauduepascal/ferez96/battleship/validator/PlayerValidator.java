package com.hauduepascal.ferez96.battleship.validator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerValidator {
    public static void checkPlayerDir(Path dir) throws Exception {
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
}
