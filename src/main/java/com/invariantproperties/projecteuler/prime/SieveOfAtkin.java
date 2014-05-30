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


/*
 * Singleton Implementation of Sieve Of Atkin. It is used to determine primality.
 *
 * @author bgiles
 */
public enum SieveOfAtkin {SIEVE;

    private int sieveSize;
    private byte[] sieve;
    private final byte[] masks;

    private SieveOfAtkin() {
        masks = new byte[8];

        for (int i = 0; i < 8; i++) {
            masks[i] = (byte) (1 << i);
        }

        sieveSize = 15485865;
        sieve = initialize(sieveSize);
    }

    /**
     * Return an instance with at least as many elements as requested.
     *
     * @param sieveSize
     * @return
     */

    /*
    public static SieveOfAtkin getInstance(int sieveSize) {
            // resize sieve if we require more elements than in current sieve.
            // this is synchronized to avoid race conditions. It's unlikely anyway
            // since we set the sieve before the sieveSize but the optimizer may
            // change the order of execution.
            if (sieveSize > INSTANCE.sieveSize) {
                    SieveOfAtkin s = new SieveOfAtkin(sieveSize);
                    synchronized(INSTANCE) {
                            INSTANCE.sieve = s.sieve;
                            INSTANCE.sieveSize = s.sieveSize;
                    }
            }

            return INSTANCE;
    }
    */

    /**
     * Initialize the sieve.
     */
    private byte[] initialize(int sieveSize) {
        byte[] sieve = new byte[(sieveSize + 7) / 8];

        // data is initialized to zero
        long sqrt = Math.round(Math.ceil(Math.sqrt(sieveSize)));

        for (int x = 0; x < sqrt; x++) {
            for (int y = 0; y < sqrt; y++) {
                int x2 = x * x;
                int y2 = y * y;

                int n = (4 * x2) + y2;

                if ((n < sieveSize) && (((n % 12) == 1) || ((n % 12) == 5))) {
                    //sieve = sieve.flipBit(n);
                    sieve[n / 8] ^= masks[n % 8];
                }

                n -= x2;

                if ((n < sieveSize) && ((n % 12) == 7)) {
                    //sieve = sieve.flipBit(n);
                    sieve[n / 8] ^= masks[n % 8];
                }

                n -= (2 * y2);

                if ((x > y) && (n < sieveSize) && ((n % 12) == 11)) {
                    //sieve = sieve.flipBit(n);
                    sieve[n / 8] ^= masks[n % 8];
                }
            }
        }

        for (int x = 5; x < sqrt; x += 2) {
            if ((sieve[x / 8] & masks[x % 8]) != 0) {
                int step = (int) (x * x);

                for (int y = step; y < sieveSize; y += step) {
                    //sieve = sieve.clearBit(n);
                    sieve[y / 8] &= ~masks[y % 8];
                }
            }
        }

        // final little bits since this sieve doesn't work for n < 5.
        sieve[0] &= ~masks[1];
        sieve[0] |= masks[2];
        sieve[0] |= masks[3];

        int size = 2; // 2, 3

        for (int n = 5; n < sieveSize; n++) {
            if ((sieve[n / 8] & masks[n % 8]) != 0) {
                size++;
            }
        }

        return sieve;
    }

    public synchronized boolean isPrime(int n) {
        if (!((0 <= n) && (n < sieveSize))) {
            throw new IllegalArgumentException("value must be between 0 and " +
                sieveSize);
        }

        return (sieve[n / 8] & masks[n % 8]) != 0;

        // return cache.testBit(n);
    }
}
