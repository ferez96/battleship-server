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
import java.util.*;

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

    public void phase1() {
        System.out.println("=== phase 1: Dau gia tau");

        //load 5 ships
        Ship[] ships = new Ship[Global.N_SHIPS];
        try (Scanner sc = new Scanner(Global.FIELD_PATH.resolve("ships.txt"))) {
            for (int i = 0; i < ships.length; ++i) {
                int hp = sc.nextInt(), atk = sc.nextInt(), range = sc.nextInt();
                ships[i] = new Ship(1, hp, atk, range, null);
                Log.info("Found ship: " + ships[i].toBeautifulString());
            }
        } catch (IOException e) {
            Log.warn("Can not load ships, random created some");
            for (int i = 0; i < ships.length; ++i) ships[i] = new Ship();
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
        for (int i = 0; i < Global.N_SHIPS; ++i) {
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

    public void phase2() {
        System.out.println();
        System.out.println("======= phase 2 =========");

        // Initialize map 8x8 no rocks
        pg = new Playground(8, 0);

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
                    try (Scanner scInp = new Scanner(p.RootDir.resolve("SET.INP"));
                         Scanner scOut = new Scanner(p.RootDir.resolve("SET.OUT"))) {
                        int colorId = scOut.nextInt();
                        if (colorId != p.Color.id) {
                            System.out.println("Wrong color id:" + colorId + ", expect:" + p.Color.id);
                            System.exit(1);
                        }
                        int lb = colorId == TeamColor.White.id ? 1 : 5;
                        int up = colorId == TeamColor.White.id ? 4 : 8;
                        int nShips = scInp.nextInt();
                        scInp.nextInt(); // ignore enemy ships
                        scInp.nextInt(); // ignore team id
                        int hp[] = new int[nShips];
                        int atk[] = new int[nShips];
                        int range[] = new int[nShips];
                        Position pos[] = new Position[nShips];
                        boolean flag[] = new boolean[nShips];
                        for (int i = 0; i < nShips; ++i) {
                            hp[i] = scInp.nextInt();
                            atk[i] = scInp.nextInt();
                            range[i] = scInp.nextInt();
                            int x = scOut.nextInt(), y = scOut.nextInt();
                            pos[i] = Position.get(x, y);
                            flag[i] = true;
                        }
                        for (int i = 0; i < nShips; ++i)
                            for (int j = i + 1; j < nShips; ++j)
                                if (pos[i].equals(pos[j])) {
                                    flag[i] = false;
                                    flag[j] = false;
                                }
                        for (int i = 0; i < nShips; ++i) {
                            if (flag[i]) {
                                Ship ship = new Ship(i, hp[i], atk[i], range[i], null);
                                ship.setPlayground(pg);
                                ship.pos = pos[i];
                                pg.set(pos[i], ship);
                                p.addShip(ship);
                                Log.info(String.format("Player %s placed ship %s at %s", p.Name, ship.toBeautifulString(), pos[i]));
                            }
                        }
                    } catch (IOException ex) {
                        Log.error("", ex);
                    }
                }

                System.out.println("End phase 2");
                pg.prettyPrint(System.out);
                writeReport();
            }
        } else System.exit(1);
    }

    public void phase3() {
        PrintStream logPs = null;
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
            for (int i = 0; i < p1.getShipsCount(); ++i) if (p1.getShip(i).hp > 0) map1.println(p1.getShip(i).toMapInpString());
            for (int i = 0; i < p2.getShipsCount(); ++i) if (p2.getShip(i).hp > 0) map2.println(p2.getShip(i).toMapInpString());
            map2.printf("%d %d %d\n", p2.getAliveShipsCount(), p1.getAliveShipsCount(), p2.Color.id);
            for (int i = 0; i < p2.getShipsCount(); ++i) if (p2.getShip(i).hp > 0) map2.println(p2.getShip(i).toMapInpString());
            for (int i = 0; i < p1.getShipsCount(); ++i) if (p1.getShip(i).hp > 0) map1.println(p1.getShip(i).toMapInpString());

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
            while (sc.hasNextInt()) prices.add(sc.nextInt());
        } catch (IOException ex) {
            // must pass
        }
        return prices;
    }
}
