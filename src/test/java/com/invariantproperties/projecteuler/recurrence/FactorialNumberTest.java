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

import org.junit.Assert;
import org.junit.Test;

import com.invariantproperties.projecteuler.recurrence.FactorialNumber;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


/**
 * Test Factorial class.
 *
 * @author bgiles
 */
public class FactorialNumberTest extends AbstractRecurrenceSequenceTest<BigInteger> {
    /**
     * Constructor.
     */
    public FactorialNumberTest() throws NoSuchMethodException {
        super(FactorialNumber.class);
    }

    /**
     * Get number of tests to run.
     */
    @Override
    public int getMaxTests() {
        return 20;
    }

    /**
     * Verify the definition is properly implemented.
     *
     * @return
     */
    @Test
    @Override
    public void verifyDefinition() {
        for (int n = 1; n < getMaxTests(); n++) {
            BigInteger u = (BigInteger) seq.get(n);
            BigInteger v = ((BigInteger) seq.get(n - 1)).multiply(BigInteger.valueOf(
                        n));
            Assert.assertEquals(u, v);
        }
    }

    /**
     * Verify initial terms.
     */
    @Test
    @Override
    public void verifyInitialTerms() {
        List<BigInteger> elements = Arrays.asList(ONE, ONE, TWO, SIX,
                BigInteger.valueOf(24), BigInteger.valueOf(120),
                BigInteger.valueOf(720));
        verifyInitialTerms(elements);
    }
}
