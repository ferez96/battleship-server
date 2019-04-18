package com.hauduepascal.ferez96.battleship.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerValidator {
    private static final Logger Log = LoggerFactory.getLogger(PlayerValidator.class);

    public static void checkPlayerDir(Path dir) throws Exception {
        if (!Files.isDirectory(dir)) throw new Exception(dir + " is not a directory.");
        List<String> sourceFiles = Files.list(dir)
                .map(p -> p.getFileName().toString().toUpperCase())
                .filter(name -> name.endsWith(".CPP"))
                .collect(Collectors.toList());
        Log.trace("Check dir found source files: " + sourceFiles);
        if (sourceFiles.stream().noneMatch(s -> s.equals("SET.CPP")))
            throw new FileNotFoundException("SET.CPP is not found");
        if (sourceFiles.stream().noneMatch(s -> s.equals("PLAY.CPP")))
            throw new FileNotFoundException("PLAY.CPP is not found");
    }
}
