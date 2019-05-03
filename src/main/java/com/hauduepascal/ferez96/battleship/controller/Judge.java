package com.hauduepascal.ferez96.battleship.controller;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.cmd.*;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

public class Judge {
    public static enum GameStatus {Processing, Done, Cancelled}

    private static final class MiniIdZen {
        static private long cnt = 0;

        static long nextId() {
            return cnt++;
        }
    }

    private static final Logger Log = LoggerFactory.getLogger(Judge.class);
    private static final ConfigurationProvider Conf = Global.Conf;

    private final long GAME_ID;
    private Player[] p; // players
    private Playground pg;
    private Path dir;
    private Properties prop = new Properties();
    private GameStatus status;


    public Judge() {
        this.GAME_ID = MiniIdZen.nextId();
        p = new Player[2];
    }

    public void importPlayers(Player p1, Player p2) {
        this.p[0] = p1;
        this.p[1] = p2;
        this.dir = Paths.get(Conf.getProperty("db_home", String.class)).resolve("games").resolve(String.valueOf(GAME_ID));
        boolean check = true;
        for (Player p : p) check = check && p.prepare(this.dir.resolve(p.Color.toString()));
        this.status = check ? GameStatus.Processing : GameStatus.Cancelled;
    }

    public boolean setup() {
        try {
            Files.createDirectories(this.dir);
            Files.createDirectories(p[0].playDir);
            Files.createDirectories(p[1].playDir);
            Files.createDirectories(this.dir.resolve("results"));
        } catch (IOException ex) {
            Log.error("Can not create data directories: " + this.dir, ex);
            return false;
        }

        // Check and compile players source codes
        for (Player p : p) {
            Path dir = p.RootDir;
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                Path auction_cpp, set_cpp, play_cpp;
                try {
                    List<Path> files = Files.list(dir).collect(Collectors.toList());
                    Optional<Path> opt_auction_cpp = files.stream().filter((x) -> x.getFileName().toString().equals("AUCTION.CPP")).findAny();
                    Optional<Path> opt_set_cpp = files.stream().filter((x) -> x.getFileName().toString().equals("SET.CPP")).findAny();
                    Optional<Path> opt_play_cpp = files.stream().filter((x) -> x.getFileName().toString().equals("PLAY.CPP")).findAny();

                    if ((opt_auction_cpp.isPresent() && opt_set_cpp.isPresent() && opt_play_cpp.isPresent())) {
                        auction_cpp = opt_auction_cpp.get();
                        set_cpp = opt_set_cpp.get();
                        play_cpp = opt_play_cpp.get();
                    } else return false;
                } catch (IOException ex) {
                    return false;
                }

                // compile source codes
                for (Path src : new Path[]{auction_cpp, set_cpp, play_cpp}) {
                    try {
                        Log.info("Compile source file: " + src);
                        int rv = Utils.compileCpp(src, this.dir.resolve(p.Color.toString()));
                        if (rv != 0) {
                            Log.warn("Compile file " + src + " error, return: " + rv);
                            return false;
                        }
                    } catch (IOException ex) {
                        Log.warn("Compile file " + src + " fail", ex);
                        return false;
                    }
                }
            } else return false;
        }


        // Export properties
        try (FileOutputStream fos = new FileOutputStream(this.dir.resolve("info.properties").toFile())) {
            prop.setProperty("game_id", "" + GAME_ID);
            for (Player player : p) prop.setProperty("player." + player.Color, String.valueOf(player.Id));
            prop.store(fos, "Created by HauDuePascal-JudgeSystem");
        } catch (IOException ex) {
            Log.error("Can not print info", ex);
            return false;
        }

        return true;
    }

    public void phase1() {
        if (status == GameStatus.Processing) {
            Log.info("Start phase 1");

            // prepare
            Map<Player, List<Integer>> prices = new HashMap<>();
            for (Player p : p) {
                try {
                    Files.copy(dir.resolve("SHIPS"), p.playDir.resolve("SHIP.INP"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Log.error("Can not copy ship.inp", ex);
                    this.cancel();
                    return;
                }

                // Exec
                try {
                    int rv;
                    CommandLine cmd = new CommandLine(p.playDir.resolve("bin/AUCTION.EXE").toString());
                    DefaultExecutor exec = new DefaultExecutor();
                    exec.setWorkingDirectory(p.playDir.toFile());
                    exec.setWatchdog(new ExecuteWatchdog(1000));
                    exec.setStreamHandler(new PumpStreamHandler(new FileOutputStream(p.playDir.resolve("log/AUCTION.run.log").toFile())));
                    Log.info("Execute command: " + cmd);
                    rv = exec.execute(cmd);
                    // TODO: Check return values
                    if (rv != 0) Log.error("Compile error return " + rv);
                } catch (Exception ex) {
                    Log.error("Execute cause error", ex);
                }

                // Read player output
                File auction_out = p.playDir.resolve("AUCTION.OUT").toFile();
                try (BufferedReader rd = new BufferedReader(new FileReader(auction_out))) {
                    String line = rd.readLine();
                    List<Integer> result = Arrays.stream(line.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
                    boolean validAnswer = result.size() == Global.N_SHIPS;
                    for (int x : result) validAnswer &= x >= 0;
                    int sum = 0;
                    for (int x : result) sum += x;
                    validAnswer &= sum <= Global.N_SHIPS * 10;

                    if (validAnswer) prices.put(p, result);
                } catch (FileNotFoundException ex) {
                    Log.error("Can not find AUCTION.OUT", ex);
                    // TODO: this player will be eliminated
                } catch (IOException ex) {
                    Log.error("Read result fail", ex);
                }

                try {
                    Files.move(p.playDir.resolve("AUCTION.OUT"), p.playDir.resolve("log/AUCTION"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    //
                }
            }

            // Judge
            if (prices.size() == 2) {
                Integer[] price0 = prices.get(p[0]).toArray(new Integer[Global.N_SHIPS]);
                Integer[] price1 = prices.get(p[1]).toArray(new Integer[Global.N_SHIPS]);
                boolean[] buy0 = new boolean[Global.N_SHIPS];
                boolean[] buy1 = new boolean[Global.N_SHIPS];
                for (int i = 0; i < Global.N_SHIPS; ++i) {
                    buy0[i] = true;
                    buy1[i] = true;
                    if (price0[i] < price1[i]) buy1[i] = false;
                    if (price0[i] > price1[i]) buy0[i] = false;
                }

                try (Scanner sc = new Scanner(p[0].playDir.resolve("SHIP.INP"))) {
                    int n = sc.nextInt();
                    for (int i = 0; i < n; ++i) {
                        int hp, atk, range, move;
                        hp = sc.nextInt();
                        atk = sc.nextInt();
                        range = sc.nextInt();
                        move = sc.nextInt();
                        if (buy0[i]) {
                            Ship ship = new Ship(p[0].Id * 10 + i, hp, atk, range, move, p[0]);
                            Log.trace("Player " + p[0].Color + " bought ship " + ship);
                            p[0].Ships.put(ship, Position.ZERO);
                        }
                    }
                } catch (IOException e) {
                    // assume no error
                }

                try (Scanner sc = new Scanner(p[1].playDir.resolve("SHIP.INP"))) {
                    int n = sc.nextInt();
                    for (int i = 0; i < n; ++i) {
                        int hp, atk, range, move;
                        hp = sc.nextInt();
                        atk = sc.nextInt();
                        range = sc.nextInt();
                        move = sc.nextInt();
                        if (buy1[i]) {
                            Ship ship = new Ship(p[1].Id * 10 + i, hp, atk, range, move, p[1]);
                            Log.trace("Player " + p[1].Color + " bought ship " + ship);
                            p[1].Ships.put(ship, Position.ZERO);
                        }
                    }
                } catch (IOException e) {
                    // assume no error
                }
            } else {
                Player winner = prices.keySet().iterator().next();
                this.status = GameStatus.Done;
                Log.info("Player " + winner + " win the game");
            }
            try {
                Files.move(p[0].playDir.resolve("SHIP.INP"), p[0].playDir.resolve("log/SHIP.INP"), StandardCopyOption.REPLACE_EXISTING);
                Files.move(p[1].playDir.resolve("SHIP.INP"), p[1].playDir.resolve("log/SHIP.INP"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                //
            }
        } else Log.info("Phase 1 skipped");
    }

    public void phase2() {
        int n;
        if (status == GameStatus.Processing) {
            Log.info("Start phase 2");

            // Load playground
            n = Global.SIZE;
            for (int i = 1; i <= n; ++i) for (int j = 1; j <= n; ++j) Position.get(i, j);
            try (BufferedReader br = new BufferedReader(new FileReader(this.dir.resolve("MAP").toFile()))) {
                List<Position> rocks = new ArrayList<>();
                for (int x = 1; x <= n; ++x) {
                    char[] line = br.readLine().toCharArray();
                    for (int y = 1; y <= n; ++y) {
                        if (line[y - 1] == '#') {
                            rocks.add(Position.get(x, y));
                            Log.trace("Load Rock(" + x + "," + y + ")");
                        }
                    }
                }

                this.pg = new Playground(Global.SIZE, rocks.toArray(new Position[rocks.size()]));
            } catch (IOException e) {
                this.cancel();
                return;
            }

            // Prepare
            for (int _i : new int[]{0, 1}) {
                Player p = this.p[_i];
                Player _p = this.p[1 - _i];
                // Export SET.INP
                File set_inp = p.playDir.resolve("SET.INP").toFile();
                try (PrintStream ps = new PrintStream(set_inp)) {
                    Set<Ship> ships = p.Ships.keySet();
                    Set<Ship> _ships = _p.Ships.keySet();

                    assert p.getShipsCount() == ships.size();
                    assert _p.getShipsCount() == _ships.size();
                    ps.printf("%d %d %d %d\n", p.getShipsCount(), _p.getShipsCount(), pg.getRockCount(), p.Color.id); // warn, getShipCount must return Ships.size()
                    for (Ship ship : ships) ps.printf("%d %d %d %d\n", ship.hp, ship.atk, ship.range, ship.move);
                    for (Ship ship : _ships) ps.printf("%d %d %d %d\n", ship.hp, ship.atk, ship.range, ship.move);
                    for (Position pos : pg.Rocks) ps.printf("%d %d\n", pos.x, pos.y);
                } catch (FileNotFoundException e) {
                    Log.error("", e);
                    // assume good
                }

                // Exec
                try {
                    CommandLine cmd = new CommandLine(p.playDir.resolve("bin/SET.EXE").toString());
                    int rv = Utils.exec(cmd, p.playDir.toFile(), p.playDir.resolve("log/SET.run.log").toFile(), 1000);
                    if (rv != 0) Log.error("Execute error return value: " + rv);
                } catch (Exception ex) {
                    Log.error("Execute cause error", ex);
                }

                // Checker
                n = Global.N_SHIPS;
                File set_out = p.playDir.resolve("SET.OUT").toFile();
                Map<Ship, Position> answer = new LinkedHashMap<>();
                Set<Ship> ships = p.Ships.keySet();
                try (Scanner sc = new Scanner(set_out)) {
                    for (Ship ship : ships) {
                        int lb = p.Color == TeamColor.WHITE ? 1 : 26;
                        int up = p.Color == TeamColor.WHITE ? 25 : 50;
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        if (x >= lb && x <= up && y >= 1 && y <= 50) {
                            Position pos = Position.get(x, y);
                            answer.put(ship, pos);
                        } else {
                            Log.trace("Player " + p.Color + " set an invalid position: (" + x + ", " + y + ")");
                            ship.destroy();
                        }
                    }
                } catch (IOException e) {
                    Log.error("Can not find " + set_out);
                    // TODO: this player will be eliminated
                }

                try {
                    Files.move(p.playDir.resolve("SET.INP"), p.playDir.resolve("log/SET.INP"), StandardCopyOption.REPLACE_EXISTING);
                    Files.move(p.playDir.resolve("SET.OUT"), p.playDir.resolve("log/SET.OUT"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    //
                }
                // Place ship
                Map<Position, Boolean> validPosition = new HashMap<>();
                for (Position pos : pg.Rocks) validPosition.put(pos, false); // Rocks is invalid pos
                for (Position pos : answer.values()) {
                    boolean valid = !validPosition.containsKey(pos); // duplicate pos is invalid
                    validPosition.put(pos, valid);
                }
                for (Ship ship : answer.keySet()) {
                    Position pos = answer.get(ship);
                    if (validPosition.get(pos)) {
                        pg.set(pos, ship);
                        ship.addShipDestroyListener(pg);
                        Log.trace("Player " + p.Color + " put " + ship + " at " + pos);
                    } else {
                        Log.trace("Player " + p.Color + " set an invalid position:" + pos);
                        ship.destroy();
                    }
                }
            }

            for (Player p : p)
                if (p.getAliveShipsCount() == 0) {
                } // TODO: Eliminate player
//            pg.prettyPrint(System.out);
        } else Log.info("Phase 2 skipped");
    }

    public void phase3() {
        if (status == GameStatus.Processing) {
            Log.info("Start phase 3");

            for (int Turn = 1; Turn <= 500; ++Turn) {
                for (Ship ship : p[0].Ships.keySet()) ship.renewStatus();
                for (Ship ship : p[1].Ships.keySet()) ship.renewStatus();

                List<ICommand> commands = new ArrayList<>();
                for (int _i : new int[]{0, 1}) {
                    Player p = this.p[_i];
                    Player _p = this.p[1 - _i];

                    int n = p.getAliveShipsCount();
                    int z = pg.getRockCount();
                    // Prepare
                    File map_inp = p.playDir.resolve("MAP.INP").toFile();
                    try (PrintStream ps = new PrintStream(map_inp)) {
                        ps.printf("%d %d\n", n, z);
                        for (Ship ship : p.Ships.keySet()) {
                            if (ship.hp > 0) {
                                Position pos = p.Ships.get(ship);
                                ps.printf("%d %d %d %d %d %d %d\n", ship.hp, ship.atk, ship.range, ship.move, pos.x, pos.y, ship.status);
                            }
                        }
                        for (Position pos : pg.Rocks) {
                            ps.printf("%d %d\n", pos.x, pos.y);
                        }
                        Log.info("Write " + map_inp);
                    } catch (IOException e) {
                        Log.error("", e);
                    }
                    File report_inp = p.playDir.resolve("REPORT.INP").toFile();
                    try (PrintStream ps = new PrintStream(report_inp)) {
                        ps.println(p.History.size());
                        p.History.forEach(ps::println);
                        Log.info("Write " + report_inp);
                    } catch (IOException e) {
                        Log.error("", e);
                    }

                    // Exec
                    try {
                        CommandLine cmd = new CommandLine(p.playDir.resolve("bin/PLAY.EXE").toString());
                        int rv = Utils.exec(cmd, p.playDir.toFile(), p.playDir.resolve(String.format("log/PLAY-%03d.run.log", Turn)).toFile(), 5000);
                        if (rv != 0) Log.error("Execute error return value: " + rv);
                    } catch (Exception ex) {
                        Log.error("Execute cause error", ex);
                    }

                    // Checker
                    String cmd = null;
                    File decision_out = p.playDir.resolve("DECISION.OUT").toFile();
                    try (Scanner sc = new Scanner(decision_out)) {
                        cmd = sc.nextLine();
                    } catch (Exception e) {
                        Log.error("", e);
                    }

                    try {
                        Files.move(p.playDir.resolve("MAP.INP"), p.playDir.resolve("log/MAP_" + Turn), StandardCopyOption.REPLACE_EXISTING);
                        Files.move(p.playDir.resolve("DECISION.OUT"), p.playDir.resolve("log/DECISION_" + Turn), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        //
                    }

                    ICommand command = ICommand.getInstance(cmd);
                    p.prepare(command);
                    if (command != null) {
                        Log.trace("Turn " + Turn + ": Player " + p.Id + " decided command " + cmd);
                        if (command.isValidCommand()) commands.add(command);
                    } else {
                        Log.trace("Turn " + Turn + ": Player " + p.Id + " decided unknown command " + cmd);
                    }
                }

                //Process commands
                if (commands.size() == 2) {
                    ICommand cmd1 = commands.get(0);
                    ICommand cmd2 = commands.get(1);

                    if (cmd1 instanceof Move && cmd2 instanceof Move) moveBoth((Move) cmd1, (Move) cmd2);
                    else if (cmd1 instanceof Normal && cmd2 instanceof Normal) fireBoth((Normal) cmd1, (Normal) cmd2);
                    else {
                        if (cmd1 instanceof Move) move((Move) cmd1);
                        if (cmd2 instanceof Move) move((Move) cmd2);
                        if (cmd1 instanceof Fire) fire((Fire) cmd1);
                        if (cmd2 instanceof Fire) fire((Fire) cmd2);
                    }
                } else {
                    ICommand cmd = commands.get(0);
                    if (cmd instanceof Move) move((Move) cmd);
                    if (cmd instanceof Fire) fire((Fire) cmd);
                }

                try (PrintStream ps = new PrintStream(dir.resolve("results/Turn_" + Turn).toFile())) {
                    for (Player p : p) {
                        for (Ship ship : p.Ships.keySet()) {
                            ps.printf("%d %d %d %d %d\n", ship.id, ship.hp, ship.atk, ship.range, ship.move);
                        }
                    }
                    for (ICommand command : commands) ps.println(command.plain());
                    for (int i = 1; i <= 50; ++i) {
                        for (int j = 1; j <= 50; ++j) {
                            Playground.ICell cell = pg.get(Position.get(i, j));
                            if (cell == Playground.BLANK_CELL) {
                                ps.println(0);
                            } else if (cell == Playground.ROCK) {
                                ps.println(-1);
                            } else if (cell instanceof Ship) {
                                ps.println(((Ship) cell).id);
                            } else {
                                ps.println();
                            }
                        }
                    }
                    Log.info("Write turn result");
                } catch (FileNotFoundException e) {
                    //
                }
            }

        } else Log.info("Phase 3 skipped");
    }

    private void cancel() {
        this.status = GameStatus.Cancelled;
    }

    private void moveBoth(Move move1, Move move2) {
        List<Position> path1 = move1.getMovePath();
        List<Position> path2 = move2.getMovePath();

        boolean crashed = false;
        for (Position pos1 : path1)
            for (Position pos2 : path2) {
                if (pos1.equals(pos2)) {
                    Ship ship1 = move1.getShip();
                    Ship ship2 = move2.getShip();
                    int dmg = Math.min(ship1.hp, ship2.hp);
                    Log.info("Ship crashed");
                    ship1.takeDamage(dmg);
                    ship2.takeDamage(dmg);
                    crashed = true;
                }
            }

        if (!crashed) {
            move(move1);
            move(move2);
        }
    }

    private void move(Move move) {
        if (!move.isValidCommand()) return;

        String s = move.getS();
        Ship ship = move.getShip();
        Position pos = move.getPos();
        pg.set(pos, Playground.BLANK_CELL);
        for (int i = 1; i <= Math.min(ship.move, s.length()); ++i) {
            pos = Move.getNextPosition(pos, s.charAt(i - 1));
            Log.trace("Ship " + ship + " try to move to " + pos);
            if (pos != null) {
                Playground.ICell target = pg.get(pos);
                if (target instanceof Playground.Rock) {
                    Log.trace("Ship " + ship + " crashed rock at " + pos);
                    ship.destroy();
                    break;
                } else if (target instanceof Ship) {
                    Log.trace("Ship " + ship + " crashed with " + target);
                    Ship other = (Ship) target;
                    int dmg = Math.min(ship.hp, other.hp);
                    ship.takeDamage(dmg);
                    other.takeDamage(dmg);
                    break;
                }
            } else {
                ship.destroy();
                break;
            }
        }

        if (ship.hp > 0) {
            Log.trace("Ship " + ship + " now locates at " + pos);
            pg.set(pos, ship);
        }

    }

    private void fire(Fire fire) {
        if (fire == null) return;
        if (fire instanceof Normal) {
            Normal cmd = (Normal) fire;

            Position pos = cmd.getPos();
            Position target = cmd.getTarget();
            Ship ship = cmd.getShip();

            int dist = Utils.manhattanDistance(pos, target);
            if (dist <= ship.range) {
                Log.trace("Ship " + ship + " fire " + target);
                Playground.ICell cell = pg.get(target);
                if (cell instanceof Ship) {
                    cmd.success();
                    int dmg = (int) Math.ceil(ship.atk * ship.hp / 10);
                    Log.trace("Ship " + ship + " make " + dmg + " damage to " + cell);
                    ((Ship) cell).takeDamage(dmg, true);
                }
            } else {
                Log.trace("Can not fire, " + target + " is too far from " + pos);
            }
        } else if (fire instanceof Flare) {
            Flare cmd = (Flare) fire;
            Position tl = cmd.getTop_left();
            Position br = cmd.getBottom_right();
            int cnt = 0;
            for (int x = tl.x; x <= br.y; ++x) {
                for (int y = tl.y; y <= br.y; ++y) {
                    Playground.ICell cell = pg.get(Position.get(x, y));
                    if (cell instanceof Ship) {
                        Ship ship = (Ship) cell;
                        if (!ship.getOwner().equals(cmd.getPlayer())) cnt++;
                    }
                }
            }
            cmd.setCount(cnt);
            Log.trace("Flare found " + cnt + "enemy ship in range [" + tl + " " + br + "]");
        } else if (fire instanceof Rocket) {
            Rocket cmd = (Rocket) fire;
            for (int i = 1; i <= 50; ++i) {
                Position pos = cmd.getDirection() == 1 ? Position.get(cmd.getIndex(), i) : Position.get(i, cmd.getIndex());
                if (pg.get(pos) instanceof Ship) {
                    Log.trace("Rocket fire " + pos);
                    ((Ship) pg.get(pos)).takeDamage(5);
                }
            }
        }
    }

    private void fireBoth(Normal f1, Normal f2) {
        Map<Ship, Integer> pendingDmg = new HashMap<>();
        for (Normal cmd : new Normal[]{f1, f2}) {
            Position pos = cmd.getPos();
            Position target = cmd.getTarget();
            Ship ship = cmd.getShip();

            int dist = Utils.manhattanDistance(pos, target);
            if (dist <= ship.range) {
                Log.trace("Ship " + ship + " fire " + target);
                Playground.ICell cell = pg.get(target);
                if (cell instanceof Ship) {
                    cmd.success();
                    int dmg = (int) Math.ceil(ship.atk * ship.hp / 10);
                    pendingDmg.put((Ship) cell, dmg);
                    Log.trace("Ship " + ship + " make " + dmg + " damage to " + cell);
                }
            } else {
                Log.trace("Can not fire, " + target + " is too far from " + pos);
            }
        }
        for (Ship ship : pendingDmg.keySet()) {
            ship.takeDamage(pendingDmg.get(ship), true);
        }
    }

}
