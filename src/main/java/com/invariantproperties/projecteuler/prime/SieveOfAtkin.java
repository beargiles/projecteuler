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
import java.util.BitSet;
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
 * @author Bear Giles <bgiles@coyotesong.com>
 */
public enum SieveOfAtkin implements Iterable<Integer> {
    SIEVE;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Integer> primecache = new ArrayList<Integer>();

    private final BitSet sieve = new BitSet();

    private SieveOfAtkin() {
        // initialize with first million primes - 15485865
        // initialize with first 10k primes - 104729
        initialize(104729);

        // initialize cache with first 10k primes for quick access
        try {
            lock.readLock().lock();
            for (int index = 0, n = 2; index < 10000; n++) {
                if (sieve.get(n)) {
                    primecache.add(n);
                    index++;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Initialize the sieve.
     */
    private void initialize(int sieveSize) {

        // actual sieve size
        int sqrt = (int) Math.round(Math.ceil(Math.sqrt(sieveSize)));
        int bitSetSize = sqrt * sqrt;

        // data is initialized to false
        sieve.set(0, bitSetSize + 1, false);

        // implementation note: the published algorithm uses a
        // map containing bits corresponding to even numbers. This
        // implementation excludes those bits.
        for (int x = 0; x < sqrt; x++) {
            for (int y = 0; y < sqrt; y++) {
                int x2 = x * x;
                int y2 = y * y;

                int n = (4 * x2) + y2;

                if ((n < sieveSize) && (((n % 12) == 1) || ((n % 12) == 5))) {
                    sieve.flip(n / 2);
                }

                n -= x2;

                if ((n < sieveSize) && ((n % 12) == 7)) {
                    sieve.flip(n / 2);
                }

                n -= (2 * y2);

                if ((x > y) && (n < sieveSize) && ((n % 12) == 11)) {
                    sieve.flip(n / 2);
                }
            }
        }

        for (int x = 5; x < sqrt; x += 2) {
            if (sieve.get(x)) {
                int step = (int) (x * x);

                for (int y = step; y < sieveSize; y += step) {
                    sieve.clear(y / 2);
                }
            }
        }

        // final little bits since this sieve doesn't work for n < 5.
        // sieve.set(2, true);
        sieve.set(3 / 2, true);
    }

    /**
     * Reinitialize the sieve. This method is called when it is necessary to
     * grow the bitset.
     */
    private void reinitialize(int n) {
        try {
            lock.writeLock().lock();
            // allocate 50% more than required to minimize thrashing.
            initialize((3 * n) / 2);
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

        if (n < 2) {
            return false;
        }

        if (n == 2) {
            return true;
        }

        // is it necessary to resize
        if (n > sieve.size()) {
            reinitialize(n);
        }

        boolean value = false;
        try {
            lock.readLock().lock();
            value = sieve.get(n / 2);
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
            try {
                lock.readLock().lock();
                while (true) {
                    offset++;

                    // do we need to force an expansion?
                    if (offset >= sieve.size()) {
                        reinitialize(offset);
                    }

                    if (isPrime(offset)) {
                        return offset;
                    }
                }
            } finally {
                lock.readLock().unlock();
            }

            // we'll always find a value since we dynamically resize the sieve.
        }

        /**
         * @see com.invariantproperties.projecteuler.AbstractListIterator#getPrevious()
         */
        @Override
        protected Integer getPrevious() {
            try {
                lock.readLock().lock();
                while (offset > 0) {
                    offset--;
                    if (isPrime(offset)) {
                        return offset;
                    }
                }
            } finally {
                lock.readLock().unlock();
            }

            // we only get here if something went horribly wrong
            throw new NoSuchElementException();
        }
    }
}
