package com.hauduepascal.ferez96.battleship.validator;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AuctionValidatorTest {

    @Test
    public void checkPrices() {
        AuctionValidator.Instance.checkPrices(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 10));
        AuctionValidator.Instance.checkPrices(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 10, 10));
        AuctionValidator.Instance.checkPrices(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 9, 11));
    }

    @Test
    public void checkFileFormat() {
        try {
            Files.createDirectories(Paths.get(".tmp"));
        } catch (IOException e) {
            //do nothing
        }
        try (PrintStream ps = new PrintStream(".tmp/auction1.txt")) {
            ps.println("10 10 10 10 10 10 10 10 10 10");
            assertTrue(AuctionValidator.Instance.checkFileFormat(new File(".tmp/auction1.txt")));
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }

        try {
            Files.delete(Paths.get(".tmp/auction1.txt"));
        } catch (IOException e) {
            System.err.println("Did not delete file \".tmp/auction1.txt\"");
        }
    }

    @Test
    public void checkFileFormat1() {
        try {
            Files.createDirectories(Paths.get(".tmp"));
        } catch (IOException e) {
            //do nothing
        }
        try (PrintStream ps = new PrintStream(".tmp/auction1.txt")) {
            ps.println("10 10 10 10 10 10 10 10 10 10\n");
            assertTrue(AuctionValidator.Instance.checkFileFormat(new File(".tmp/auction1.txt")));
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
        try {
            Files.delete(Paths.get(".tmp/auction1.txt"));
        } catch (IOException e) {
            System.err.println("Did not delete file \".tmp/auction1.txt\"");
        }
    }

    @Test
    public void checkFileFormat2() {
        try {
            Files.createDirectories(Paths.get(".tmp"));
        } catch (IOException e) {
            //do nothing
        }
        try (PrintStream ps = new PrintStream(".tmp/auction1.txt")) {
            ps.println("10 10 10 10 10 10 10 10 10 10\nabc");
            assertFalse(AuctionValidator.Instance.checkFileFormat(new File(".tmp/auction1.txt")));
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
        try {
            Files.delete(Paths.get(".tmp/auction1.txt"));
        } catch (IOException e) {
            System.err.println("Did not delete file \".tmp/auction1.txt\"");
        }
    }
}