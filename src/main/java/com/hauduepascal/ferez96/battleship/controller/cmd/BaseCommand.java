package com.hauduepascal.ferez96.battleship.controller.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

abstract public class BaseCommand {
    protected static Logger Log = LoggerFactory.getLogger(BaseCommand.class);

    public boolean validCommand = true;
    protected int code;

    abstract public void _import(Scanner scanner);

    abstract public String plain();

    abstract public ResultData execute();

    public final BaseCommand create(Scanner scanner) {
        int cmdType = scanner.nextInt();
        if (cmdType == 0) return new Move();
        if (cmdType == 1) {
            int fireType = scanner.nextInt();
            if (fireType == 1) return new Flare();
            if (fireType == 2) return new Rocket();
            if (fireType == 3) return new Normal();
        }
        Log.info("Command Type Not Valid: " + cmdType);
        return null;
    }

    @Override
    public String toString() {
        return validCommand ? plain() : "-1";
    }
}

class ResultData {

}