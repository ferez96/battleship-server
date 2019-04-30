package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Position;

import java.util.Scanner;

public class Move extends BaseCommand {
    public Position pos;
    private char h;

    public Move() {
        this.code = 0;
    }

    @Override
    public void _import(Scanner scanner) {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        pos = Position.get(x, y);
        this.h = scanner.next().charAt(0);
        validCommand = h == 'L' || h == 'X' || h == 'T' || h == 'P';
    }

    @Override
    public String plain() {
        return String.format("%s %s %s %s", code, pos.x, pos.y, h);
    }

    @Override
    public ResultData execute() {
        return null;
    }

    public Position getNextPosition() {
        switch (h) {
            case 'L':
                return Position.get(pos.x - 1, pos.y);
            case 'X':
                return Position.get(pos.x + 1, pos.y);
            case 'T':
                return Position.get(pos.x, pos.y - 1);
            case 'P':
                return Position.get(pos.x, pos.y + 1);
            default:
                return null;
        }
    }
}
