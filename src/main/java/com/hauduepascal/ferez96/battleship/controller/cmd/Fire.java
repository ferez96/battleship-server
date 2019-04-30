package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Position;

import java.util.Scanner;

public abstract class Fire extends BaseCommand {
    public Position src;
    public Position target;

    public Fire() {
        this.code = 1;
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

    @Override
    public ResultData execute() {
        return null;
    }
}

class Flare extends Fire{

}

class Rocket extends Fire{

}

class Normal extends Fire{

}