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

import com.invariantproperties.projecteuler.recurrence.PellNumber;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;


/**
 * Test Pell class.
 * @author bgiles
 */
public class PellNumberTest extends AbstractRecurrenceSequenceTest<BigInteger> {
    /**
     * Constructor
     */
    public PellNumberTest() throws NoSuchMethodException {
        super(PellNumber.class);
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
        for (int n = 2; n < getMaxTests(); n++) {
            BigInteger u = seq.get(n);
            BigInteger v = seq.get(n - 1);
            BigInteger w = seq.get(n - 2);
            Assert.assertEquals(u, TWO.multiply(v).add(w));
        }
    }

    /**
     * Verify initial terms.
     */
    @Test
    @Override
    public void verifyInitialTerms() {
        verifyInitialTerms(Arrays.asList(ZERO, ONE, TWO, FIVE, TWELVE,
                BigInteger.valueOf(29), BigInteger.valueOf(70)));
    }
}
