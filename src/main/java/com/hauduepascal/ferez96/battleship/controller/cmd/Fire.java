package com.hauduepascal.ferez96.battleship.controller.cmd;

import com.hauduepascal.ferez96.battleship.controller.Position;

import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class Fire extends BaseCommand {
    Fire(String cmd) {
        this.code = 1;
    }

    @Override
    public ResultData execute() {
        return null;
    }
}

class Flare extends Fire {
    private static final Pattern PATTERN = Pattern.compile("1\\s1(\\s([1-9]|[1-4][0-9]|50)){4}$");

    Flare(String cmd) {
        super(cmd);
    }

    static public boolean match(String cmd) {
        return PATTERN.matcher(cmd).matches();
    }

    @Override
    public String plain() {
        return null;
    }
}

class Rocket extends Fire {
    private static final Pattern PATTERN = Pattern.compile("1\\s2\\s[12]\\s([1-9]|[1-4][0-9]|50)$");

    Rocket(String cmd) {
        super(cmd);
    }

    static public boolean match(String cmd) {
        return PATTERN.matcher(cmd).matches();
    }

    @Override
    public String plain() {
        return null;
    }
}

class Normal extends Fire {
    private static final Pattern PATTERN = Pattern.compile("1\\s3(\\s([1-9]|[1-4][0-9]|50)){4}$");

    Normal(String cmd) {
        super(cmd);
    }

    static public boolean match(String cmd) {
        return PATTERN.matcher(cmd).matches();
    }

    @Override
    public String plain() {
        return null;
    }
}