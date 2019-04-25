package com.hauduepascal.ferez96.battleship.controller;

import java.util.Scanner;

abstract public class BaseCommand {
    boolean validCommand = true;
    int code;

    abstract public void _import(Scanner scanner);

    abstract public String plain();

    @Override
    public String toString() {
        return validCommand ? plain() : "-1";
    }
}

