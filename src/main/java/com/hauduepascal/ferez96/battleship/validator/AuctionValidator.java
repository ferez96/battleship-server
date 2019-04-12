package com.hauduepascal.ferez96.battleship.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AuctionValidator {

    public static final AuctionValidator Instance = new AuctionValidator();

    public boolean checkPrices(List<Integer> prices) {
        return prices != null && prices.size() == 10 && prices.stream().mapToInt(x -> x).sum() <= 100;
    }

    public boolean checkFileFormat(File file) {
        try (FileInputStream stream = new FileInputStream(file)) {
            Scanner scanner = new Scanner(stream);
            for (int i = 0; i < 10; ++i) {
                int x = scanner.nextInt();
                if (x < 0 || x > 100) return false;
            }
            return !scanner.hasNext();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return false;
        }
    }
}
