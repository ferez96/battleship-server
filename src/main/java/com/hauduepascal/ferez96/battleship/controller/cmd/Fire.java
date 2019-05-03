package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Position;
import com.hauduepascal.ferez96.battleship.controller.Ship;

import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class Fire extends BaseCommand {
    Fire(String cmd) {
        this.code = 1;
    }

}

