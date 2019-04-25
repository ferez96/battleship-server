package com.hauduepascal.ferez96.battleship.common;

import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.controller.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    public static Map<String, String> parseArgs(String args[]) {
        Map<String, String> ans = new LinkedHashMap<>();
        for (int i = 0; i < args.length; ) {
            String cur = args[i];

            // long param
            if (cur.startsWith("--")) {
                ans.put(cur.substring(2), args[i + 1]);
                i += 2;
                continue;
            }

            // short param
            if (cur.startsWith("-")) {
                ans.put(cur.substring(1), args[i + 1]); // not handled duplicate key
                i += 2;
                continue;
            }

            // default
            {
                ans.put(cur, null);
                i += 1;
            }
        }
        return ans;
    }

    public static void pressEnter2Continue() {
        try {
            System.in.skip(10);
            System.out.println("Press 'Enter' to continue");
            System.in.read();
        } catch (IOException ex) {
            Log.error("Standard IO Fail", ex);
            System.exit(1);
        }
    }


    public static int compileCpp(Player p, String fileName) {
        try {
            String cmd = "g++ --static -o2 -std=c++14 -o " + p.RootDir.resolve(fileName) + ".exe " + p.RootDir.resolve(fileName + ".cpp");
            System.out.println("Execute cmd: " + cmd);
            Process compiler = Runtime.getRuntime().exec(cmd);
            Files.copy(compiler.getInputStream(), p.RootDir.resolve(fileName + ".compiler.out"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(compiler.getErrorStream(), p.RootDir.resolve(fileName + ".compiler.err"), StandardCopyOption.REPLACE_EXISTING);
            return compiler.waitFor();
        } catch (IOException | InterruptedException ex) {
            Log.error("Compile error", ex);
            return -1;
        }
    }

    public static int runExe(Player p, String fileName) {
        try {
            String cmd = p.RootDir.resolve(fileName + ".exe").toString();
            System.out.println("Execute cmd: " + cmd);
            Process compiler = Runtime.getRuntime().exec(cmd, null, p.RootDir.toFile());
            Files.copy(compiler.getInputStream(), p.RootDir.resolve(fileName + ".run.out"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(compiler.getErrorStream(), p.RootDir.resolve(fileName + ".run.err"), StandardCopyOption.REPLACE_EXISTING);
            return compiler.waitFor();
        } catch (IOException | InterruptedException ex) {
            Log.error("Execute fail", ex);
            return -1;
        }
    }
}
