package com.hauduepascal.ferez96.battleship.validator;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AuctionValidatorTest {
    Logger Log = LoggerFactory.getLogger(AuctionValidatorTest.class);

    @Before
    public void before() {
        try {
            Files.createDirectories(Paths.get(".tmp"));
        } catch (IOException e) {
            //do nothing
        }
    }

    @Test
    public void checkPrices() {
        AuctionValidator.Instance.checkPrices(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 10));
        AuctionValidator.Instance.checkPrices(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 10, 10));
        AuctionValidator.Instance.checkPrices(Arrays.asList(10, 10, 10, 10, 10, 10, 10, 10, 9, 11));
    }

    @Test
    public void checkFileFormat() {
        try (PrintStream ps = new PrintStream(".tmp/auction1.txt")) {
            ps.println("10 10 10 10 10 10 10 10 10 10");
            assertTrue(AuctionValidator.Instance.checkFileFormat(new File(".tmp/auction1.txt")));
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }

        try {
            Files.delete(Paths.get(".tmp/auction1.txt"));
        } catch (IOException e) {
            Log.warn("Did not delete file \".tmp/auction1.txt\"", e);
        }
    }

    @Test
    public void checkFileFormat1() {
        try (PrintStream ps = new PrintStream(".tmp/auction1.txt")) {
            ps.println("10 10 10 10 10 10 10 10 10 10\n");
            assertTrue(AuctionValidator.Instance.checkFileFormat(new File(".tmp/auction1.txt")));
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
        try {
            Files.delete(Paths.get(".tmp/auction1.txt"));
        } catch (IOException e) {
            Log.warn("Did not delete file \".tmp/auction1.txt\"", e);
        }
    }

    @Test
    public void checkFileFormat2() {
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