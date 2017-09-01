package com.lvonce.binaryweaver;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
public class StackTraceTest {
    private static final TimeMeasurer measurer = TimeMeasurerFactory.getTimeMeasurer(StackTraceTest.class);
    public static int add(int x, int y) {
        measurer.begin("add", x, y);
        int result = x + y;
        measurer.end("add");
        return result;
    }

    @Test 
    public void testStackTrace() {
        for (int i=0; i<2000; ++i) {
           add(23, 21); 
        } 


        int trials = 1_000_0;

        long start = System.currentTimeMillis();

        long a = 1;
        for (int i = 0; i < trials; i += 1) {
            a += 1;
        }

        long duration = System.currentTimeMillis() - start;
        System.out.println("Simple loop took " + duration + " ms");

        start = System.currentTimeMillis();

        a = 1;
        for (int i = 0; i < trials; i += 1) {
            a += 1;
            Thread.currentThread().getId();
        }

        duration = System.currentTimeMillis() - start;
        System.out.println("Getting current thread took " + duration + " ms");

        start = System.currentTimeMillis();

        a = 1;
        for (int i = 0; i < trials; i += 1) {
            a += 1;
            Thread.currentThread().getStackTrace();
        }

        duration = System.currentTimeMillis() - start;
        System.out.println("Getting stack trace took " + duration + " ms");

        start = System.currentTimeMillis();

        a = 1;
        for (int i = 0; i < trials; i += 1) {
            a += 1;
            (new Throwable()).getStackTrace();
        }

        duration = System.currentTimeMillis() - start;
        System.out.println("Getting throwable stack trace took " + duration + " ms");
    }
    // public static void main(String... args) {
    // }
}