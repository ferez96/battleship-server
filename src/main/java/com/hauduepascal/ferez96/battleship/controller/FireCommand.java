package com.hauduepascal.ferez96.battleship.controller;

import java.util.Scanner;

public class FireCommand extends BaseCommand {
    Position src;
    Position target;

    {
        code = 1;
    }

    @Override
    public void _import(Scanner scanner) {
        int x1 = scanner.nextInt();
        int y1 = scanner.nextInt();
        int x2 = scanner.nextInt();
        int y2 = scanner.nextInt();
        src = Position.get(x1, y1);
        target = Position.get(x2, y2);
    }

    @Override
    public String plain() {
        return String.format("%s %s %s %s %s", code, src.x, src.y, target.x, target.y);
    }
}
