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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.invariantproperties.projecteuler.AbstractListIterator;

/*
 * Singleton Implementation of Sieve Of Atkin. It is used to determine primality. 
 * The internal cache is resized as necessary so this class may consume a large
 * amount of memory, roughly n/5 where n is the largest number checked for primality.
 * 
 * If you want the largest prime divisor you should use SieveOfEratostheses.SIEVE.
 *
 * Implementation note: for larger sieves it may be necessary to use explicit
 * byte arrays instead of a BitSet.
 *
 * @author Bear Giles <bgiles@coyotesong.com>
 */
public enum SieveOfAtkin implements Iterable<Integer> {
    SIEVE;
    private final byte MASK[] = new byte[8];
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Integer> primecache = new ArrayList<Integer>();

    private byte[] sieve;

    private SieveOfAtkin() {
        for (int i = 0; i < 8; i++) {
            MASK[i] = (byte) (1 << i);
        }

        // initialize with first million primes - 15485865
        // initialize with first 10k primes - 104729
        sieve = initialize(104729);

        // initialize cache with first 10k primes for quick access
        primecache.add(2);
        for (int n = 3; primecache.size() < 10000; n += 2) {
            if (this.isPrime(n)) {
                primecache.add(n);
            }
        }
    }

    private void flip(int n) {
        assert n >= 0;

        int offset = n / 8;
        sieve[offset] = (byte) (sieve[offset] ^ MASK[n % 8]);
    }

    private void set(int n) {
        assert n >= 0;

        int offset = n / 8;
        sieve[offset] = (byte) (sieve[offset] | MASK[n % 8]);
    }

    private void clear(int n) {
        assert n >= 0;

        int offset = n / 8;
        sieve[offset] = (byte) (sieve[offset] & ~MASK[n % 8]);
    }

    private boolean getBit(int n) {
        assert n >= 0;

        int offset = n / 8;
        return (sieve[offset] & MASK[n % 8]) != 0;
    }

    /**
     * Initialize the sieve.
     */
    private byte[] initialize(int sieveSize) {
        // actual sieve size
        int sqrt = (int) Math.round(Math.ceil(Math.sqrt(sieveSize)));
        int actualSieveSize = 8 * ((sqrt * sqrt + 7) / 8);

        sieve = new byte[actualSieveSize / 8];

        for (int x = 1; x < sqrt; x++) {
            for (int y = 1; y < sqrt + 1; y++) {
                int x2 = x * x;
                int y2 = y * y;

                int n = 4 * x2 + y2;

                if ((n < actualSieveSize) && (((n % 12) == 1) || ((n % 12) == 5))) {
                    flip(n);
                }

                n -= x2;

                if ((n < actualSieveSize) && ((n % 12) == 7)) {
                    flip(n);
                }

                n -= 2 * y2;

                if ((x > y) && (n < actualSieveSize) && ((n % 12) == 11)) {
                    flip(n);
                }
            }
        }

        for (int x = 5; x < sqrt + 1; x += 2) {
            if (getBit(x)) {
                int step = (int) (x * x);

                for (int y = step; y < actualSieveSize; y += step) {
                    clear(y);
                }
            }
        }

        // final little bits since this sieve doesn't work for n < 5.
        set(2);
        set(3);
        set(5);

        return sieve;
    }

    /**
     * Reinitialize the sieve. This method is called when it is necessary to
     * grow the bitset.
     */
    private void reinitialize(int n) {
        assert n > 0;

        lock.writeLock().lock();
        try {
            // allocate 50% more than required to minimize thrashing.
            sieve = initialize((3 * n) / 2);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Is this a prime number?
     * 
     * @param n
     * @return true if prime
     * @throws IllegalArgumentException
     *             if negative number
     */
    public boolean isPrime(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("value must be non-zero");
        }

        if (n == 1) {
            return false;
        }

        if (n == 2 || n == 3) {
            return true;
        }

        if (n % 2 == 0) {
            return false;
        }

        // is it necessary to resize
        if (8 * sieve.length < n) {
            reinitialize(n);
        }

        boolean value = false;
        try {
            lock.readLock().lock();
            value = getBit(n);
        } finally {
            lock.readLock().unlock();
        }

        return value;
    }

    /**
     * @see java.util.List#get(int)
     * @param n
     * @return nth prime (starting with 2)
     * @throws IllegalArgumentException
     *             if negative number
     */
    public Integer get(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("value must be non-zero");
        }

        // easy way
        if (n < primecache.size()) {
            return primecache.get(n);
        }

        // hard way
        Iterator<Integer> iter = iterator();
        for (int i = 0; i < n; i++) {
            iter.next();
        }

        return iter.next();
    }

    /**
     * @see java.util.List#indexOf(java.lang.Object)
     * 
     * @param n
     *            potentially prime number
     * @return index of prime (starting with 2), or -1 if non-prime.
     * @throws IllegalArgumentException
     *             if negative number
     */
    public int indexOf(Integer n) {
        if (n < 0) {
            throw new IllegalArgumentException("value must be non-zero");
        }

        if (!isPrime(n)) {
            return -1;
        }

        // easy way
        if (n < primecache.get(primecache.size() - 1)) {
            return primecache.indexOf(n);
        }

        // hard way
        int index = 0;
        for (int i : SIEVE) {
            if (i == n) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Integer> iterator() {
        return new AtkinListIterator();
    }

    public ListIterator<Integer> listIterator() {
        return new AtkinListIterator();
    }

    /**
     * List iterator.
     * 
     * @author Bear Giles <bgiles@coyotesong.com>
     */
    class AtkinListIterator extends AbstractListIterator<Integer> {
        int offset;

        /**
         * @see com.invariantproperties.projecteuler.AbstractListIterator#getNext()
         */
        @Override
        protected Integer getNext() {
            while (true) {
                offset++;

                if (isPrime(offset)) {
                    return offset;
                }
            }

            // we'll always find a value since we dynamically resize the sieve.
        }

        /**
         * @see com.invariantproperties.projecteuler.AbstractListIterator#getPrevious()
         */
        @Override
        protected Integer getPrevious() {
            while (offset > 0) {
                offset--;
                if (isPrime(offset)) {
                    return offset;
                }
            }

            // we only get here if something went horribly wrong
            throw new NoSuchElementException();
        }
    }
}
