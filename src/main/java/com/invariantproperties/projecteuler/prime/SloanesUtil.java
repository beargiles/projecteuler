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

import java.io.PrintWriter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * To add: - A006530 - Gpf(n) - greatest prime factor (a(1) = 1) - A007947 -
 * rad(n) - largest squarefree number dividing n, radical of n - A003557 - n
 * divided by radial (largest squarefree divisor of n, n >= 1 - A008472 -
 * sopf(n) - sum of distinct prime factors of n
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 * 
 */
public class SloanesUtil {
    private static final int SIEVE_SIZE = 10000000 + 1;
    private static int[] greatestFactor;
    private static final List<Integer> seeds = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19);
    private static final LinkedHashMap<Integer, Map<Integer, Integer>> factorCache;

    static {
        factorCache = new LinkedHashMap<Integer, Map<Integer, Integer>>() {
            protected boolean removeEldestElement(Map.Entry<Integer, Integer> entry) {
                return factorCache.size() > 10;
            }
        };
    }

    static {
        initialize(SIEVE_SIZE);
    }

    private static void initialize(int size) {
        greatestFactor = new int[size];

        // wheel factorization (sort of...)
        greatestFactor[1] = 1;

        for (Integer seed : seeds) {
            for (int i = seed; i < size; i += seed) {
                greatestFactor[i] = seed;
            }
        }

        // now do modified Sieve of Er...
        int sqrt = (int) Math.floor(Math.sqrt(size) + 1);

        for (int prime = 2; prime < sqrt; prime++) {
            if (0 == greatestFactor[prime]) {
                for (int i = prime; i < size; i += prime) {
                    greatestFactor[i] = prime;
                }
            }
        }

        // now flesh out the rest of the sieve.
        for (int i = sqrt; i < size; i++) {
            if (0 == greatestFactor[i]) {
                greatestFactor[i] = i;
            }
        }
    }

    /**
     * Factor a number.
     * <p>
     * Implementation note: this method uses an internal cache so repeated calls
     * with the same argument will usually not incur a significant cost.
     * 
     * @param n
     * @return
     */
    public static Map<Integer, Integer> factor(int n) {
        Map<Integer, Integer> map = null;

        synchronized (factorCache) {
            Integer N = n;

            if (factorCache.containsKey(N)) {
                map = factorCache.get(N);
            } else {
                map = new TreeMap<Integer, Integer>();

                for (int i = n; i > 1;) {
                    int factor = greatestFactor[i];

                    if (map.containsKey(factor)) {
                        map.put(factor, 1 + map.get(factor));
                    } else {
                        map.put(factor, 1);
                    }

                    i /= factor;
                }

                factorCache.put(N, map);
            }
        }

        return map;
    }

    public static String listFactors(int n) {
        StringBuilder sb = new StringBuilder(10);
        Map<Integer, Integer> map = factor(n);

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                sb.append(String.format(", %d", entry.getKey()));
            } else {
                sb.append(String.format(", %d^%d", entry.getKey(), entry.getValue()));
            }
        }

        return sb.substring(2);
    }

    /**
     * Get number of prime factors. (&omega;) (A001221)
     * 
     * @param n
     * @return
     */
    public static int getOmega(int n) {
        Map<Integer, Integer> map = factor(n);

        return map.keySet().size();
    }

    /**
     * Get total number of factors (&Omega;). (A001222)
     * 
     * @param n
     * @return
     */
    public static int getNumberOfFactors(int n) {
        int sum = 0;
        Map<Integer, Integer> map = factor(n);

        for (Integer i : map.values()) {
            sum += i;
        }

        return sum;
    }

    /**
     * Get totient value (&phi;, A0000??)
     * 
     * @param n
     * @return
     */
    public static long getTotient(int n) {
        Map<Integer, Integer> map = factor(n);
        long phi = 1;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                phi *= (entry.getKey() - 1);
            } else {
                phi *= ((entry.getKey() - 1) * Math.round(Math.pow(entry.getKey(), entry.getValue() - 1)));
            }
        }

        return phi;
    }

    /**
     * Get number of divisors (&sigma;, A000010)
     * 
     * @param n
     * @return
     */
    public static long getSigma(int n) {
        Map<Integer, Integer> map = factor(n);
        long sigma = 1;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int sum = 1;

            long x = entry.getKey();

            for (int exp = 1; exp <= entry.getValue(); exp++, x *= entry.getKey()) {
                sum += x;
            }

            sigma *= sum;
        }

        return sigma;
    }

    /**
     * Get restricted number of divisors(s(n), A001065)
     * 
     * @param n
     * @return
     */
    public static long getRestrictedNumberOfDivisors(int n) {
        return getSigma(n) - n;
    }

    public static void dump() {
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("COPY basic (n, gfp, ...)");
        pw.println("  FROM STDIN");

        for (int n = 2; n < greatestFactor.length; n++) {
            pw.printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\t%s%n", n, greatestFactor[n], getOmega(n), getNumberOfFactors(n),
                    getTotient(n), getSigma(n), getRestrictedNumberOfDivisors(n), listFactors(n));
        }

        pw.println("\\.");
        pw.flush();
    }

    public static void main(String[] args) {
        /*
         * for (int i = 2; i < greatestFactor.length; i++) { if (i ==
         * greatestFactor[i]) { System.out.println(i); } }
         */
        dump();
    }
}
