package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.validator.AuctionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Judge {

    private Player p1, p2;
    private static final Logger Log = LoggerFactory.getLogger(Judge.class);


    public void importPlayers(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void phrase1() {
        System.out.println("=== Phrase 1: Dau gia tau");

        //load ships
        Ship[] ships = new Ship[10];
        try (Scanner sc = new Scanner(Global.FIELD_PATH.resolve("ships.txt"))) {
            for (int i = 0; i < 10; ++i) {
                int hp = sc.nextInt(), atk = sc.nextInt(), range = sc.nextInt();
                ships[i] = new Ship(hp, atk, range);
            }
        } catch (IOException e) {
            Log.warn("Can not load ships, random created some");
            for (int i = 0; i < 10; ++i) ships[i] = new Ship();
        }
        // TODO: print loaded ships

        // Load prices
        Path f1 = p1.RootDir.resolve("prices.txt");
        Path f2 = p2.RootDir.resolve("prices.txt");
        AuctionValidator.Instance.checkFileFormat(f1.toFile());
        AuctionValidator.Instance.checkFileFormat(f2.toFile());

        List<Integer> prices1 = importPrices(f1);
        List<Integer> prices2 = importPrices(f2);
        AuctionValidator.Instance.checkPrices(prices1);
        AuctionValidator.Instance.checkPrices(prices2);

        // Calculating
        System.out.println("======  Result ======");
        List<Ship> s1 = new ArrayList<>(), s2 = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            int pr1 = prices1.get(i);
            int pr2 = prices2.get(i);
            if (pr1 < pr2) {
                System.out.println("Player " + p1.Color + " get " + ships[i]);
                s1.add(ships[i]);
            }
            if (pr1 > pr2) {
                System.out.println("Player " + p2.Color + " get " + ships[i]);
                s2.add(ships[i]);
            }
            if (pr1 == pr2) {
                System.out.println("Both player get " + ships[i]);
                s1.add(ships[i]);
                s2.add(ships[i]);
            }
        }

        // Write result into SET.INP
        try (PrintStream ps1 = new PrintStream(p1.RootDir.resolve("SET.inp").toFile());
             PrintStream ps2 = new PrintStream(p2.RootDir.resolve("SET.inp").toFile())) {
            ps1.printf("%d %d %d\n", s1.size(), s2.size(), p1.Color.id);
            s1.forEach(ps1::println);
            ps2.printf("%d %d %d\n", s2.size(), s1.size(), p2.Color.id);
            s2.forEach(ps2::println);
        } catch (IOException e) {
            Log.error("Write SET.INP fail!", e);
        }
    }

    public void phrase2() {
        Log.info("compiler " + p1.Name + " return " + Utils.compileCpp(p1, "SET"));
        Log.info("run " + p1.Name + " return " + Utils.runExe(p1, "SET"));
        Log.info("compiler " + p2.Name + " return " + Utils.compileCpp(p2, "SET"));
        Log.info("run " + p2.Name + " return " + Utils.runExe(p2, "SET"));
    }

    public void phrase3() {
//        Log.info("compiler " + p1.Name + " return " + Utils.compileCpp(p1, "MAP"));
//        Log.info("run " + p1.Name + " return " + Utils.runExe(p1, "MAP"));
//        Log.info("compiler " + p2.Name + " return " + Utils.compileCpp(p2, "MAP"));
//        Log.info("run " + p2.Name + " return " + Utils.runExe(p2, "MAP"));
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
