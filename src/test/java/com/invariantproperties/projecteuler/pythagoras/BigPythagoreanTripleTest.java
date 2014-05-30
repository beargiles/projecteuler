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

import com.invariantproperties.projecteuler.pythagoras.BigPythagoreanTriple;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
public class BigPythagoreanTripleTest {
    private static final int MAX_TESTS = 1000;
    private static final int MAX_DEPTH = 8;
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);
    private static final BigInteger TWELVE = BigInteger.valueOf(12);
    private static final BigInteger SIXTY = BigInteger.valueOf(60);

    /**
     * Verify definition
     */
    @Test
    public void verifyDefinition() {
        BigInteger x = ZERO;

        for (int i = 0; i < MAX_TESTS; i++) {
            BigPythagoreanTriple t = BigPythagoreanTriple.getInstance(i);
            assertEquals(t.getC().pow(2), t.getA().pow(2).add(t.getB().pow(2)));
            org.junit.Assert.assertTrue(t.getC().pow(2)
                                         .compareTo(BigInteger.valueOf(
                        Long.MAX_VALUE)) < 0);
            x = x.max(t.getC());
        }

        System.out.println(x);
    }

    /**
     * Verify elliptic curve (also a Diophantine equation)
     */
    @Test
    public void verifyEllipticCurve() {
        for (int i = 0; i < MAX_TESTS; i++) {
            BigPythagoreanTriple t = BigPythagoreanTriple.getInstance(i);
            //			System.out.printf("%d: (%s, %s); (%s, %s, %s); (%s, %s, %s)%n", i,
            //					t.getM(), t.getN(), t.getA(), t.getB(), t.getC(), t.getY(),
            //					t.getX(), t.getD());
            assertEquals(t.getY().pow(2),
                t.getX().pow(3).subtract(t.getX().multiply(t.getD())));
        }
    }

    /**
     * Verify divisibility
     */
    @Test
    public void verifyDivisibility() {
        for (int i = 0; i < MAX_TESTS; i++) {
            BigPythagoreanTriple t = BigPythagoreanTriple.getInstance(i);
            assertEquals(ZERO, t.getA().multiply(t.getB()).remainder(TWELVE));
            assertEquals(ZERO,
                t.getA().multiply(t.getB()).multiply(t.getC()).remainder(SIXTY));
        }
    }

    /**
     * Verify mutual primality.
     */
    @Test
    public void verifyPrimality() {
        for (int i = 0; i < MAX_TESTS; i++) {
            BigPythagoreanTriple t = BigPythagoreanTriple.getInstance(i);
            assertEquals(ONE, t.getA().gcd(t.getB().gcd(t.getC())));
        }
    }

    /**
     * Verify that getInstance() index matches recursive definition
     */
    @Test
    public void verifyIndex() {
        recurse(0, ZERO, TWO, ONE);
    }

    /**
     * Recursively walk the tree.
     *
     * @param depth
     * @param idx
     * @param m
     * @param n
     */
    private void recurse(int depth, BigInteger idx, BigInteger m, BigInteger n) {
        if (depth > MAX_DEPTH) {
            return;
        }

        BigPythagoreanTriple t = BigPythagoreanTriple.getInstance(idx.intValue());
        assertEquals(m, t.getM());
        assertEquals(n, t.getN());
        recurse(depth + 1, THREE.multiply(idx).add(ONE),
            TWO.multiply(m).subtract(n), m); // U
        recurse(depth + 1, THREE.multiply(idx).add(TWO),
            TWO.multiply(m).add(n), m); // A
        recurse(depth + 1, THREE.multiply(idx).add(THREE),
            m.add(TWO.multiply(n)), n); // D
    }
}
