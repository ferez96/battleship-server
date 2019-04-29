package com.hauduepascal.ferez96.battleship.enums;

public enum TeamColor {
    Black(2), White(1);

    public final int id;

    TeamColor(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id == 2 ? "black" : "white";
    }
}
