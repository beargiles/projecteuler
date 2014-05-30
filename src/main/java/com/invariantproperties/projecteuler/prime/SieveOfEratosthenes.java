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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/*
 * Singleton Implementation of modified Sieve Of Eratosthenes. This sieve
 * keeps track of the largest prime factor, not just primality.
 *
 * @author bgiles
 */
public enum SieveOfEratosthenes {SIEVE;

    private int sieveSize;
    private int[] sieve;

    private SieveOfEratosthenes() {
        sieveSize = 15485865;
        //sieveSize = 1000;
        sieve = initialize(sieveSize);
    }

    /**
     * Initialize the sieve.
     */
    private int[] initialize(int sieveSize) {
        int[] sieve = new int[sieveSize];

        // data is initialized to zero
        long sqrt = Math.round(Math.ceil(Math.sqrt(sieveSize)));

        for (int x = 2; x < sqrt; x++) {
            if (sieve[x] == 0) {
                for (int y = 2 * x; y < sieveSize; y += x) {
                    sieve[y] = x;
                }
            }
        }

        return sieve;
    }

    public synchronized boolean isPrime(int n) {
        if (!((0 <= n) && (n < sieveSize))) {
            throw new IllegalArgumentException("value must be between 0 and " +
                sieveSize);
        }

        return sieve[n] == 0;
    }

    private Map<Integer,Integer> factorize(int n) {
        if (!((0 <= n) && (n < sieveSize))) {
            throw new IllegalArgumentException("value must be between 0 and " +
                sieveSize);
        }

        Map<Integer, Integer> factors = new TreeMap<Integer, Integer>();

        for (int factor = sieve[n]; factor > 0; factor = sieve[n]) {
            if (factors.containsKey(factor)) {
                factors.put(factor, 1 + factors.get(factor));
            } else {
                factors.put(factor, 1);
            }

            n /= factor;
        }

        // must add final term
        if (factors.containsKey(n)) {
            factors.put(n, 1 + factors.get(n));
        } else {
            factors.put(n, 1);
        }

        return factors;
    }

    private String toString(Map<Integer,Integer> factors) {
        StringBuilder sb = new StringBuilder(20);

        for (Map.Entry<Integer, Integer> entry : factors.entrySet()) {
            sb.append(", ");

            if (entry.getValue() == 1) {
                sb.append(String.valueOf(entry.getKey()));
            } else {
                sb.append(String.valueOf(entry.getKey()));
                sb.append("^");
                sb.append(String.valueOf(entry.getValue()));
            }
        }

        return sb.substring(2);
    }

    public static void main(String[] args) {
        for (int n = 2; n < 100; n++) {
            System.out.printf("%d  %s%n", n, SIEVE.toString(SIEVE.factorize(n)));
        }
    }
}
