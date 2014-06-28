package com.invariantproperties.projecteuler.prime;

import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.*;

public class SieveOfAtkinTest {
    @Test
    public void testIterator() {
        Iterator<Integer> iter = SieveOfAtkin.SIEVE.iterator();
        for (int i = 0; i < 100; i++) {
            // System.out.println(iter.next());
        }

        iter = SieveOfAtkin.SIEVE.iterator();
        for (int i = 0; i < 20; i++) {
            assertTrue(SieveOfAtkin.SIEVE.isPrime(iter.next()));
        }
    }
}
