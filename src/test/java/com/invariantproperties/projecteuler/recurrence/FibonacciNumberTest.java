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
package com.invariantproperties.projecteuler.recurrence;

import com.invariantproperties.projecteuler.recurrence.FibonacciNumber;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;


/**
 * Test Fibonacci class.
 *
 * @author bgiles
 */
public class FibonacciNumberTest extends AbstractRecurrenceSequenceTest<BigInteger> {
    private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1);

    /**
     * Constructor
     */
    public FibonacciNumberTest() throws NoSuchMethodException {
        super(FibonacciNumber.class);
    }

    /**
     * Get number of tests to run.
     */
    @Override
    public int getMaxTests() {
        return 300;
    }

    /**
     * Verify the definition is properly implemented.
     *
     * @return
     */
    @Test
    @Override
    public void verifyDefinition() {
        for (int n = 2; n < getMaxTests(); n++) {
            BigInteger u = seq.get(n);
            BigInteger v = seq.get(n - 1);
            BigInteger w = seq.get(n - 2);
            Assert.assertEquals(u, v.add(w));
        }
    }

    /**
     * Verify initial terms.
     */
    @Test
    @Override
    public void verifyInitialTerms() {
        verifyInitialTerms(Arrays.asList(ZERO, ONE, ONE, TWO, THREE, FIVE, EIGHT));
    }

    /**
     * Verify that every third term is even and the other two terms are odd.
     * This is a subset of the general divisibility property.
     *
     * @return
     */
    @Test
    public void verifyEvenDivisibility() {
        for (int n = 0; n < getMaxTests(); n += 3) {
            Assert.assertEquals(ZERO, seq.get(n).mod(TWO));
            Assert.assertEquals(ONE, seq.get(n + 1).mod(TWO));
            Assert.assertEquals(ONE, seq.get(n + 2).mod(TWO));
        }
    }

    /**
     * Verify general divisibility property.
     *
     * @return
     */
    @Test
    public void verifyDivisibility() {
        for (int d = 3; d < getMaxTests(); d++) {
            BigInteger divisor = seq.get(d);

            for (int n = 0; n < getMaxTests(); n += d) {
                Assert.assertEquals(ZERO, seq.get(n).mod(divisor));

                for (int i = 1; (i < d) && ((n + i) < getMaxTests()); i++) {
                    Assert.assertFalse(ZERO.equals(seq.get(n + i).mod(divisor)));
                }
            }
        }
    }

    /**
     * Verify the property that gcd(F(m), F(n)) = F(gcd(m,n)). This is a
     * stronger statement than the divisibility property.
     */
    @Test
    public void verifyGcd() {
        for (int m = 3; m < getMaxTests(); m++) {
            for (int n = m + 1; n < getMaxTests(); n++) {
                BigInteger gcd1 = seq.get(m).gcd(seq.get(n));
                int gcd2 = BigInteger.valueOf(m).gcd(BigInteger.valueOf(n))
                                     .intValue();
                Assert.assertEquals(gcd1, seq.get(gcd2));
            }
        }
    }

    /**
     * Verify second identity (per Wikipedia): sum(F(i)) = F(n+2)-1
     */
    @Test
    public void verifySecondIdentity() {
        BigInteger sum = ZERO;

        for (int n = 0; n < getMaxTests(); n++) {
            sum = sum.add(seq.get(n));
            Assert.assertEquals(sum, seq.get(n + 2).subtract(ONE));
        }
    }

    /**
     * Verify third identity (per Wikipedia): sum(F(2i)) = F(2n+1)-1 and
     * sum(F(2i+1)) = F(2n)
     */
    @Test
    public void verifyThirdIdentity() {
        BigInteger sum = ZERO;

        for (int n = 0; n < getMaxTests(); n += 2) {
            sum = sum.add(seq.get(n));
            Assert.assertEquals(sum, seq.get(n + 1).subtract(ONE));
        }

        sum = ZERO;

        for (int n = 1; n < getMaxTests(); n += 2) {
            sum = sum.add(seq.get(n));
            Assert.assertEquals(sum, seq.get(n + 1));
        }
    }

    /**
     * Verify fourth identity (per Wikipedia): sum(iF(i)) = nF(n+2) - F(n+3) + 2
     */
    @Test
    public void verifyFourthIdentity() {
        BigInteger sum = ZERO;

        for (int n = 0; n < getMaxTests(); n++) {
            sum = sum.add(BigInteger.valueOf(n).multiply(seq.get(n)));

            BigInteger x = BigInteger.valueOf(n).multiply(seq.get(n + 2))
                                     .subtract(seq.get(n + 3)).add(TWO);
            Assert.assertEquals(sum, x);
        }
    }

    /**
     * Verify fifth identity (per Wikipedia): sum(F(i)^2) = F(n)F(n+1)
     */
    public void verifyFifthIdentity() {
        BigInteger sum = ZERO;

        for (int n = 0; n < getMaxTests(); n += 2) {
            BigInteger u = seq.get(n);
            BigInteger v = seq.get(n + 1);
            sum = sum.add(u.pow(2));
            Assert.assertEquals(sum, u.multiply(v));
        }
    }

    /**
     * Verify Cassini's Identity - F(n-1)F(n+1) - F(n)^2 = -1^n
     */
    @Test
    public void verifyCassiniIdentity() {
        for (int n = 2; n < getMaxTests(); n += 2) {
            BigInteger u = seq.get(n - 1);
            BigInteger v = seq.get(n);
            BigInteger w = seq.get(n + 1);

            BigInteger x = w.multiply(u).subtract(v.pow(2));
            Assert.assertEquals(ONE, x);
        }

        for (int n = 1; n < getMaxTests(); n += 2) {
            BigInteger u = seq.get(n - 1);
            BigInteger v = seq.get(n);
            BigInteger w = seq.get(n + 1);

            BigInteger x = w.multiply(u).subtract(v.pow(2));
            Assert.assertEquals(MINUS_ONE, x);
        }
    }

    /**
     * Verify doubling: F(2n-1) = F(n)^2 + F(n-1)^2 and F(2n) =
     * F(n)(F(n-1)+F(n+1)) = F(n)(2*F(n-1)+F(n).
     */
    @Test
    public void verifyDoubling() {
        for (int n = 1; n < getMaxTests(); n++) {
            BigInteger u = seq.get(n - 1);
            BigInteger v = seq.get(n);
            BigInteger w = seq.get(n + 1);

            BigInteger x = v.multiply(v).add(u.pow(2));
            Assert.assertEquals(seq.get((2 * n) - 1), x);

            x = v.multiply(u.add(w));
            Assert.assertEquals(seq.get(2 * n), x);

            x = v.multiply(v.add(TWO.multiply(u)));
            Assert.assertEquals(seq.get(2 * n), x);
        }
    }

    /**
     * Verify tripling.
     */
    @Test
    public void verifyTripling() {
        for (int n = 1; n < getMaxTests(); n++) {
            BigInteger u = seq.get(n - 1);
            BigInteger v = seq.get(n);
            BigInteger w = seq.get(n + 1);

            BigInteger x = TWO.multiply(v.pow(3))
                              .add(THREE.multiply(v).multiply(u).multiply(w));
            Assert.assertEquals(seq.get(3 * n), x);

            x = w.pow(3).add(THREE.multiply(w).multiply(v.pow(2)))
                 .subtract(v.pow(3));
            Assert.assertEquals(seq.get((3 * n) + 1), x);

            x = w.pow(3).add(THREE.multiply(w.pow(2)).multiply(v)).add(v.pow(3));
            Assert.assertEquals(seq.get((3 * n) + 2), x);
        }
    }
}
