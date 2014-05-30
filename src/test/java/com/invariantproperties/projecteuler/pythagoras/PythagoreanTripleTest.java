/*
 * This code was written by Bear Giles <bgiles@coyotesong.com> and he
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Any contributions made by others are licensed to this project under
 * one or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 * Copyright (c) 2014 Bear Giles <bgiles@coyotesong.com>
 */
package com.invariantproperties.projecteuler.pythagoras;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.invariantproperties.projecteuler.pythagoras.PythagoreanTriple;

import java.math.BigInteger;


public class PythagoreanTripleTest {
    private static final int MAX_TESTS = 1000;
    private static final int MAX_DEPTH = 8;

    /**
     * Verify definition
     */
    @Test
    public void verifyDefinition() {
        for (int i = 0; i < MAX_TESTS; i++) {
            PythagoreanTriple t = PythagoreanTriple.getInstance(i);
            assertEquals(t.getC() * t.getC(),
                (t.getA() * t.getA()) + (t.getB() * t.getB()));
        }
    }

    /**
     * Verify elliptic curve (also a Diophantine equation). We need to use
     * BigIntegers for these calculations since they will overflow far sooner
     * that the other calculations.
     */
    @Test
    public void verifyEllipticCurve() {
        for (int i = 0; i < MAX_TESTS; i++) {
            PythagoreanTriple t = PythagoreanTriple.getInstance(i);
            assertEquals(t.getY().pow(2),
                t.getX().pow(3).subtract(t.getX().multiply(t.getD())));
        }
    }

    /**
     * Verify divisibility.
     */
    @Test
    public void verifyDivisibility() {
        for (int i = 0; i < MAX_TESTS; i++) {
            PythagoreanTriple t = PythagoreanTriple.getInstance(i);
            assertEquals(0L, (t.getA() * t.getB()) % 12L);
            assertEquals(0L, (t.getA() * t.getB() * t.getC()) % 60L);
        }
    }

    /**
     * Verify mutual primality.
     */
    @Test
    public void verifyPrimality() {
        for (int i = 0; i < MAX_TESTS; i++) {
            PythagoreanTriple t = PythagoreanTriple.getInstance(i);
            assertEquals(BigInteger.ONE,
                BigInteger.valueOf(t.getA())
                          .gcd((BigInteger.valueOf(t.getB())
                                          .gcd(BigInteger.valueOf(t.getC())))));
        }
    }

    /**
     * Verify that getInstance() index matches recursive definition
     */
    @Test
    public void verifyIndex() {
        recurse(0, 0, 2, 1);
    }

    /**
     * Recursively walk the tree.
     *
     * @param depth
     * @param idx
     * @param m
     * @param n
     */
    private void recurse(int depth, long idx, long m, long n) {
        if (depth > MAX_DEPTH) {
            return;
        }

        PythagoreanTriple t = PythagoreanTriple.getInstance(idx);
        assertEquals(m, t.getM());
        assertEquals(n, t.getN());
        recurse(depth + 1, (3 * idx) + 1, (2 * m) - n, m); // U
        recurse(depth + 1, (3 * idx) + 2, (2 * m) + n, m); // A
        recurse(depth + 1, (3 * idx) + 3, m + (2 * n), n); // D
    }
}
