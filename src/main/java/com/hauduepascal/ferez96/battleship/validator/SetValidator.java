package com.hauduepascal.ferez96.battleship.validator;

import com.hauduepascal.ferez96.battleship.controller.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class SetValidator {
    private static final Logger Log = LoggerFactory.getLogger(SetValidator.class);

    public static boolean validPlayerShips(Player p) {
        try (Scanner scOut = new Scanner(p.RootDir.resolve("SET.OUT"))) {
            int m = p.getShipsCount(), id = p.Color.id;
            int _id = scOut.nextInt();

            if (id != _id) {
                Log.error(String.format("Input team id and output team id not match, expect: %s found: %s", id, _id));
                return false;
            }
            for (int i = 1; i <= m; ++i) {
                scOut.nextInt();
                scOut.nextInt();
            }
        } catch (IOException ex) {
            Log.error("SET.OUT did not pass validation", ex);
            return false;
        }
        return true;
    }
}
