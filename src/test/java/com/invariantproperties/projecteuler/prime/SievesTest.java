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
package com.invariantproperties.projecteuler.prime;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test Sieve of Atkin and Sieve of Eratosthenes together. The theory is that
 * the odds of both being wrong is low.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 */
public class SievesTest {

    /**
     * Verify sieves agree on primality for numbers < 100k
     */
    @Test
    public void testConsistency() {
        for (int i = 2; i < 200000; i++) {
            assertEquals(i + ": Atkin says " + SieveOfAtkin.SIEVE.isPrime(i) + " but Eratosthenes says "
                    + SieveOfEratosthenes.SIEVE.isPrime(i), SieveOfAtkin.SIEVE.isPrime(i),
                    SieveOfEratosthenes.SIEVE.isPrime(i));
        }
    }

    /**
     * Verify Atkins iterator is consistent with Sieve of Eratosthenes.
     */
    @Test
    public void testAtkinIterator() {
        int counter = 0;
        for (Integer p : SieveOfAtkin.SIEVE) {
            assertTrue(p + " should be prime.", SieveOfEratosthenes.SIEVE.isPrime(p));
            if (counter++ > 50000) {
                break;
            }
        }
    }

    /**
     * Verify Eratosthenes iterator is consistent with Sieve of Atkin.
     */
    @Test
    public void testEraosthenesIterator() {
        int counter = 0;
        for (Integer p : SieveOfEratosthenes.SIEVE) {
            assertTrue(p + " should be prime.", SieveOfAtkin.SIEVE.isPrime(p));
            if (counter++ > 50000) {
                break;
            }
        }
    }

}
