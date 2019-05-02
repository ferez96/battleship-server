package com.hauduepascal.ferez96.battleship.controller.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

abstract public class BaseCommand implements ICommand {
    protected static Logger Log = LoggerFactory.getLogger(BaseCommand.class);

    protected boolean validCommand = true;
    protected int code;

    abstract public String plain();

    abstract public ResultData execute();

    public boolean isValidCommand() {
        return validCommand;
    }

    @Override
    public String toString() {
        return validCommand ? plain() : "-1";
    }
}

interface ResultData {

}