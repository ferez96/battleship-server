package com.hauduepascal.ferez96.battleship.controller;

import java.util.Scanner;

abstract public class BaseCommand {
    boolean validCommand = true;

    abstract public void _import(Scanner scanner);

    public abstract String plain();

    @Override
    public String toString() {
        return validCommand ? plain() : "-1";
    }
}

