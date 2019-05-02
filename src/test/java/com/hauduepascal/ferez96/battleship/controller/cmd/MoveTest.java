package com.hauduepascal.ferez96.battleship.controller.cmd;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveTest {

    @Test
    public void testMatch() {
        assertTrue(Move.match("0 1 1 UDLR"));
        assertTrue(Move.match("0 50 50 UDLR"));
        assertFalse(Move.match("0 0 1 UDLR"));
        assertFalse(Move.match("0 1 0 UDLR"));
        assertFalse(Move.match("0 51 1 UDLR"));
        assertFalse(Move.match("0 1 51 UDLR"));
        assertTrue(Move.match("0 1 1 U"));
        assertTrue(Move.match("0 1 1 D"));
        assertTrue(Move.match("0 1 1 L"));
        assertTrue(Move.match("0 1 1 R"));
        assertTrue(Move.match("0 1 1 UUUUUUUUUUUU"));
    }
}