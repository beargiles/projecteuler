package com.invariantproperties.projecteuler.prime;

import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.*;

public class SieveOfEratosthenesTest {
    @Test
    public void testIterator() {
        Iterator<Integer> iter = SieveOfEratosthenes.SIEVE.iterator();
        for (int i = 0; i < 20; i++) {
            // System.out.println(iter.next());
        }

        iter = SieveOfEratosthenes.SIEVE.iterator();
        for (int i = 0; i < 20; i++) {
            assertTrue(SieveOfEratosthenes.SIEVE.isPrime(iter.next()));
        }
    }
}
