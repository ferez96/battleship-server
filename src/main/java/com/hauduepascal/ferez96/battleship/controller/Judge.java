package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.validator.AuctionValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Judge {

    private Player p1, p2;


    public void importPlayers(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void phrase1() {
        System.out.println("=== Phrase 1: Dau gia tau");
        System.out.println("==========");
        System.out.println("======");
        System.out.println("===");

        Ship[] ships = new Ship[10];
        for (int i = 0; i < 10; ++i) ships[i] = new Ship();
        for (int i = 0; i < 10; ++i) System.out.println(ships[i]);

        //
        Path f1 = p1.getRootDir().resolve("prices.txt");
        Path f2 = p2.getRootDir().resolve("prices.txt");
        AuctionValidator.Instance.checkFileFormat(f1.toFile());
        AuctionValidator.Instance.checkFileFormat(f2.toFile());

        List<Integer> prices1 = importPrices(f1);
        List<Integer> prices2 = importPrices(f2);
        AuctionValidator.Instance.checkPrices(prices1);
        AuctionValidator.Instance.checkPrices(prices2);


        System.out.println("======  Result ======");
        for (int i = 0; i < 10; ++i) {
            int pr1 = prices1.get(i);
            int pr2 = prices2.get(i);
            if (pr1 < pr2) {
                System.out.println("Player " + p1.getColor() + " get " + ships[i]);
            }
            if (pr1 > pr2) {
                System.out.println("Player " + p2.getColor() + " get " + ships[i]);
            }
            if (pr1 == pr2) {
                System.out.println("Both player get " + ships[i]);
            }
        }
    }

    private List<Integer> importPrices(Path path) {
        List<Integer> prices = null;
        try (Scanner sc = new Scanner(path)) {
            prices = new ArrayList<>();
            for (int i = 0; i < 10; ++i) prices.add(sc.nextInt());
        } catch (IOException ex) {
            // must pass
        }
        return prices;
    }
}
