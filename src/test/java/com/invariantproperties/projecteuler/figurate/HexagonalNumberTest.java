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
package com.invariantproperties.projecteuler.figurate;

import org.junit.Assert;
import org.junit.Test;

import com.invariantproperties.projecteuler.figurate.HexagonalNumber;

import java.util.Arrays;


public class HexagonalNumberTest extends AbstractFigurateTest<Long> {
    /**
     * Constructor.
     */
    public HexagonalNumberTest() throws NoSuchMethodException {
        super(HexagonalNumber.class);
    }

    /**
     * Verify the definition is properly implemented.
     *
     * @return
     */
    @Test
    @Override
    public void verifyDefinition() {
        for (int n = 0; n < getMaxTests(); n++) {
            long x = (long) (n * ((2 * n) - 1));
            Assert.assertEquals(x, seq.get(n).longValue());
        }
    }

    /**
     * Verify initial terms.
     */
    @Test
    @Override
    public void verifyInitialTerms() {
        verifyInitialTerms(Arrays.asList(0L, 1L, 6L, 15L, 28L, 45L, 66L, 91L,
                120L, 153L, 190L, 231L));
    }
}
