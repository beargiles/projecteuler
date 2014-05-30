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
import com.invariantproperties.projecteuler.recurrence.LucasNumber;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;


/**
 * Test Lucas class.
 * @author bgiles
 */
public class LucasNumberTest extends AbstractRecurrenceSequenceTest<BigInteger> {
    private static final FibonacciNumber fibonacci = new FibonacciNumber();

    /**
     * Constructor
     */
    public LucasNumberTest() throws NoSuchMethodException {
        super(LucasNumber.class);
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
        verifyInitialTerms(Arrays.asList(TWO, ONE, THREE, FOUR, SEVEN, ELEVEN,
                BigInteger.valueOf(18), BigInteger.valueOf(29)));
    }

    /**
     * Verify Lucas properties.
     */
    @Test
    public void verifyLucas() {
        // L(n) = F(n-1) + F(n+1)
        for (int n = 2; n < getMaxTests(); n++) {
            Assert.assertEquals(seq.get(n),
                fibonacci.get(n - 1).add(fibonacci.get(n + 1)));
        }
    }

    /**
     *  // F(2n) = L(n)F(n)
     *
     */
    @Test
    public void verifyLucas2() {
        for (int n = 2; n < getMaxTests(); n++) {
            Assert.assertEquals(fibonacci.get(2 * n),
                seq.get(n).multiply(fibonacci.get(n)));
        }
    }

    /**
     * // F(n) = (L(n-1)+ L(n+1))/5
     *
     */
    @Test
    public void verifyLucas3() {
        for (int n = 2; n < getMaxTests(); n++) {
            Assert.assertEquals(FIVE.multiply(fibonacci.get(n)),
                seq.get(n - 1).add(seq.get(n + 1)));
        }
    }

    /**
     * // L(n)^2 = 5 F(n)^2 + 4(-1)^n
     */
    @Test
    public void verifyLucas4() {
        for (int n = 2; n < getMaxTests(); n += 2) {
            Assert.assertEquals(seq.get(n).pow(2),
                FIVE.multiply(fibonacci.get(n).pow(2)).add(FOUR));
        }

        for (int n = 1; n < getMaxTests(); n += 2) {
            Assert.assertEquals(seq.get(n).pow(2),
                FIVE.multiply(fibonacci.get(n).pow(2)).subtract(FOUR));
        }
    }
}
