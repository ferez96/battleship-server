package com.hauduepascal.ferez96.battleship.controller.cmd;

import java.util.Scanner;

public interface ICommand {

    String plain();

    boolean isValidCommand();

    static ICommand getInstance(String cmd) {
        if (cmd == null) return null;
        if (Move.match(cmd)) return new Move(cmd);
        if (Flare.match(cmd)) return new Flare(cmd);
        if (Rocket.match(cmd)) return new Rocket(cmd);
        if (Normal.match(cmd)) return new Normal(cmd);
        return null;
    }
}
