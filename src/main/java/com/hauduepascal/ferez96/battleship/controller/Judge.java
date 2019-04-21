package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.Playground.BlankCell;
import com.hauduepascal.ferez96.battleship.controller.Playground.ICell;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.validator.AuctionValidator;
import com.hauduepascal.ferez96.battleship.validator.SetValidator;
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
    private Playground pg;
    private List<BaseCommand> Report1 = new ArrayList<>();
    private List<BaseCommand> Report2 = new ArrayList<>();
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
                ships[i] = new Ship(1, hp, atk, range, null);
                Log.info("Found ship: " + ships[i].toBeautifulString());
            }
        } catch (IOException e) {
            Log.warn("Can not load ships, random created some");
            for (int i = 0; i < 10; ++i) ships[i] = new Ship();
        }

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
                System.out.println("Player " + p1.Color + " get " + ships[i].toBeautifulString());
                s1.add(ships[i]);
            }
            if (pr1 > pr2) {
                System.out.println("Player " + p2.Color + " get " + ships[i].toBeautifulString());
                s2.add(ships[i]);
            }
            if (pr1 == pr2) {
                System.out.println("Both player get " + ships[i].toBeautifulString());
                s1.add(ships[i]);
                s2.add(ships[i]);
            }
        }
        s1.forEach(p1::addShip);
        s2.forEach(p2::addShip);

        // Write result into SET.INP
        try (PrintStream ps1 = new PrintStream(p1.RootDir.resolve("SET.INP").toFile());
             PrintStream ps2 = new PrintStream(p2.RootDir.resolve("SET.INP").toFile())) {
//            Utils.pressEnter2Continue();
            ps1.printf("%d %d %d\n", s1.size(), s2.size(), p1.Color.id);
            s1.forEach(ps1::println);
            s2.forEach(ps1::println);
            ps2.printf("%d %d %d\n", s2.size(), s1.size(), p2.Color.id);
            s2.forEach(ps2::println);
            s1.forEach(ps2::println);
        } catch (IOException e) {
            Log.error("Write SET.INP fail!", e);
        }
    }

    public void phrase2() {
        System.out.println();
        System.out.println("======= Phrase 2 =========");

        // Load map
        try (Scanner sc = new Scanner(Global.FIELD_PATH.resolve("map.txt"))) {
            int size = sc.nextInt();
            int nRock = sc.nextInt();
            pg = new Playground(size, nRock);
            sc.nextLine();
            for (int i = 1; i <= size; ++i) {
                String line = sc.nextLine();
                for (int j = 1; j <= size; ++j)
                    pg.set(Position.get(i, j), line.charAt(j - 1) == '.' ? new BlankCell() : new Playground.Rock());
            }
            System.out.println("Map loaded");
            pg.prettyPrint(System.out);
        } catch (IOException e) {
            Log.error("", e);
            pg = null;
        }

        // Load ships
        if (pg != null) { // if Playground is loaded
            System.out.println(p1.Color + " player's ships:");
            for (int i = 0; i < p1.getShipsCount(); ++i) System.out.println(p1.getShip(i).toBeautifulString());
            System.out.println(p2.Color + " player's ships:");
            for (int i = 0; i < p2.getShipsCount(); ++i) System.out.println(p2.getShip(i).toBeautifulString());
//            Utils.pressEnter2Continue();

            Log.info("run " + p1.Name + " return " + Utils.runExe(p1, "SET")); // run p1 SET.exe
            Log.info("run " + p2.Name + " return " + Utils.runExe(p2, "SET")); // run p2 SET.exe

            if (SetValidator.checkOutputFile(p1) && SetValidator.checkOutputFile(p2)) {
                for (Player p : new Player[]{p1, p2}) {
                    try (Scanner sc = new Scanner(p.RootDir.resolve("SET.OUT"))) {
                        int colorId = sc.nextInt();
                        assert (colorId == p.Color.id);
                        int lb = colorId == TeamColor.White.id ? 1 : 5;
                        int up = colorId == TeamColor.White.id ? 4 : 8;
                        for (int i = 0; i < p.getShipsCount(); ++i) {
                            Ship ship = p.getShip(i);
                            int x = sc.nextInt(), y = sc.nextInt();
                            Position pos = Position.get(x, y);
                            if (x >= lb && x <= up && y >= 1 && y <= 8 && pg.get(pos) instanceof BlankCell) {
                                pg.set(pos, ship);
                                ship.addShipDestroyListener(pg);
                                ship.pos = pos;
                                Log.info("Ship " + i + " place at " + pos);
                            } else {
                                Log.warn("Ship " + i + " can not place at " + pos + " and has been destroyed");
                                ship.destroy();
                            }
                        }
                    } catch (IOException ex) {
                        Log.error("", ex);
                    }
                }

                System.out.println("End phrase 2");
                pg.prettyPrint(System.out);
                writeReport();
            }
        } else System.exit(1);
    }

    public void phrase3() {
        PrintStream logPs = null;
        try {
            logPs = new PrintStream(Global.FIELD_PATH.resolve("result.txt").toFile());
        } catch (IOException e) {
            Log.warn("Can not create file result.txt", e);
        }
        //
        for (int t = 1; t <= 50; ++t) {
            if (p1.getAliveShipsCount() == 0 || p2.getAliveShipsCount() == 0) {
                System.out.println("One player have no ships left, game over");
                System.exit(0);
            }

            Log.info("run " + p1.Name + " return " + Utils.runExe(p1, "PLAY"));
            Log.info("run " + p2.Name + " return " + Utils.runExe(p2, "PLAY"));
            try (Scanner sc1 = new Scanner(p1.RootDir.resolve("DECISION.OUT"));
                 Scanner sc2 = new Scanner(p2.RootDir.resolve("DECISION.OUT"))) {
                int type1 = sc1.nextInt();
                int type2 = sc2.nextInt();
                BaseCommand cmd1 = type1 == 0 ? new RunCommand() : new FireCommand();
                BaseCommand cmd2 = type2 == 0 ? new RunCommand() : new FireCommand();
                cmd1._import(sc1);
                cmd2._import(sc2);
                Report1.add(cmd1);
                Report2.add(cmd2);
                Log.info("P1 command: " + cmd1.plain());
                Log.info("P2 command: " + cmd2.plain());

                if (cmd1.getClass() != cmd2.getClass()) {
                    p1.move(cmd1, pg);
                    p2.move(cmd2, pg);
                    p1.fire(cmd1, pg);
                    p2.fire(cmd2, pg);
                } else if (cmd1 instanceof RunCommand && cmd2 instanceof RunCommand) {
                    Position pos2 = ((RunCommand) cmd2).pos;
                    ICell cell2 = pg.get(pos2);
                    pg.set(pos2, Playground.BLANK_CELL);
                    p1.move(cmd1, pg);
                    p2.move(cmd2, pg);
                    if (!cmd2.validCommand)
                        if (pg.get(pos2) instanceof BlankCell) pg.set(pos2, cell2);
                        else {
                            if (pg.get(pos2) instanceof Ship) ((Ship) pg.get(pos2)).destroy();
                            if (cell2 instanceof Ship) ((Ship) cell2).destroy();
                        }
                } else if (cmd1 instanceof FireCommand && cmd2 instanceof FireCommand) {
                    p1.fire(cmd1, pg);
                    p2.fire(cmd2, pg);
                }

                System.out.println(">>> Turn " + t);
                pg.prettyPrint(System.out);
                if (logPs != null) {
                    logPs.println("    ===============\n        Turn " + t);
                    pg.prettyPrint(logPs);
                }
                writeReport();
            } catch (IOException ex) {
                Log.error("", ex);
            }
//            Utils.pressEnter2Continue();
        }
    }

    private void writeReport() {
        try (PrintStream map1 = new PrintStream(p1.RootDir.resolve("MAP.INP").toFile());
             PrintStream map2 = new PrintStream(p2.RootDir.resolve("MAP.INP").toFile());
             PrintStream report1 = new PrintStream(p1.RootDir.resolve("REPORT.INP").toFile());
             PrintStream report2 = new PrintStream(p2.RootDir.resolve("REPORT.INP").toFile())) {

            // MAP.INP
            map1.printf("%d %d %d\n", p1.getAliveShipsCount(), p2.getAliveShipsCount(), p1.Color.id);
            for (int i = 0; i < p1.getShipsCount(); ++i)
                if (p1.getShip(i).hp > 0) map1.println(p1.getShip(i).toMapInpString());
            map2.printf("%d %d %d\n", p2.getAliveShipsCount(), p1.getAliveShipsCount(), p2.Color.id);
            for (int i = 0; i < p2.getShipsCount(); ++i)
                if (p2.getShip(i).hp > 0) map2.println(p2.getShip(i).toMapInpString());


            // REPORT.INP
            report1.println(Report1.size());
            Report1.forEach(report1::println);
            report2.println(Report2.size());
            Report2.forEach(report2::println);
        } catch (IOException ex) {
            Log.error("", ex);
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
