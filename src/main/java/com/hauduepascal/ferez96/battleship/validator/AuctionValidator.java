package com.hauduepascal.ferez96.battleship.validator;

import com.hauduepascal.ferez96.battleship.app.Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AuctionValidator {

    public static final AuctionValidator Instance = new AuctionValidator();

    public boolean checkPrices(List<Integer> prices) {
        return prices != null && prices.size() == Global.N_SHIPS && prices.stream().mapToInt(x -> x).sum() <= 10 * Global.N_SHIPS;
    }

    public boolean checkFileFormat(File file) {
        try (FileInputStream stream = new FileInputStream(file)) {
            Scanner scanner = new Scanner(stream);
            while (scanner.hasNextInt()) {
                int x = scanner.nextInt();
                if (x < 0 || x > 50) return false;
            }
            return !scanner.hasNext();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return false;
        }
    }
}
