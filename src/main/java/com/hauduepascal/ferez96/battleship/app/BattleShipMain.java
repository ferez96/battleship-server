package com.hauduepascal.ferez96.battleship.app;


import com.hauduepascal.ferez96.battleship.controller.Judge;
import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.controller.Ship;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;

public class BattleShipMain {
    public static void main(String[] args) {
        System.out.println("Chuong trinh cham vong chung ket cuoc thi Hau Due Pascal");

        Player w = loadPlayer(0, TeamColor.White);
        if (w != null) System.out.println(w);
        Player b = loadPlayer(1, TeamColor.Black);
        if (b != null) System.out.println(b);

        Judge judge = new Judge();
        judge.importPlayers(b, w);
        if (args.length>2 && args[1].equals("gen-ship")) {
        	Ship[] ships = new Ship[10];
            for (int i = 0; i < 10; ++i) ships[i] = new Ship();
            for (int i = 0; i < 10; ++i) System.out.println(ships[i]);
        	return;
        }
        judge.phrase1();        

    }

    private static Player loadPlayer(long id, TeamColor color) {
        String name = "Player " + id;
        Player p = null;
        try {
            p = new Player(name, color);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return p;
    }
}
