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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.invariantproperties.projecteuler.AbstractListIterator;

/*
 * Singleton Implementation of modified Sieve Of Eratosthenes. This sieve
 * keeps track of the largest prime factor, not just primality. The internal
 * cache is resized as necessary so this class may consume a large amount of
 * memory, roughly 4*n where n is the largest number checked for primality.
 * 
 * If you only want primality you should use SieveOfAtkin.SIEVE.
 *
 * @author bgiles
 */
public enum SieveOfEratosthenes implements Iterable<Integer> {
    SIEVE;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Integer> primecache = new ArrayList<Integer>();

    private volatile int[] sieve;

    private SieveOfEratosthenes() {
        // initialize with first million primes - 15485865
        // initialize with first 10k primes - 104729
        sieve = initialize(104729);

        // initialize cache with first 10k primes for quick access
        try {
            lock.readLock().lock();
            primecache.add(2);
            for (int n = 3; primecache.size() < 10000; n += 2) {
                if (isPrime(n)) {
                    primecache.add(n);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Initialize the sieve.
     */
    private int[] initialize(int sieveSize) {
        assert sieveSize > 0;

        long sqrt = Math.round(Math.ceil(Math.sqrt(sieveSize)));
        int actualSieveSize = (int) (sqrt * sqrt);

        // data is initialized to zero
        int[] sieve = new int[actualSieveSize];

        for (int x = 2; x < sqrt; x++) {
            if (sieve[x] == 0) {
                for (int y = 2 * x; y < actualSieveSize; y += x) {
                    sieve[y] = x;
                }
            }
        }

        return sieve;
    }

    /**
     * Initialize the sieve. This method is called when it is necessary to grow
     * the sieve.
     */
    private void reinitialize(int n) {
        assert n > 0;

        try {
            lock.writeLock().lock();
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
        assert n >= 0;

        if (n < 0) {
            throw new IllegalArgumentException("value must be non-zero");
        }

        if (n >= sieve.length) {
            // may have overlapping reinitializations but that's okay.
            reinitialize(n);
        }

        boolean isPrime = false;
        try {
            lock.readLock().lock();
            isPrime = sieve[n] == 0;
        } finally {
            lock.readLock().unlock();
        }

        return isPrime;
    }

    /**
     * Factorize a number
     * 
     * @param n
     * @return map of prime divisors (key) and exponent(value)
     * @throws IllegalArgumentException
     *             if negative number
     */
    private Map<Integer, Integer> factorize(int n) {
        assert n >= 0;

        if (n < 0) {
            throw new IllegalArgumentException("value must be non-zero");
        }

        final Map<Integer, Integer> factors = new TreeMap<Integer, Integer>();

        try {
            lock.readLock().lock();
            // handle reinitialization in just one place.
            isPrime(n);

            for (int factor = sieve[n]; factor > 0; factor = sieve[n]) {
                if (factors.containsKey(factor)) {
                    factors.put(factor, 1 + factors.get(factor));
                } else {
                    factors.put(factor, 1);
                }

                n /= factor;
            }
        } finally {
            lock.readLock().unlock();
        }

        // must add final term
        if (factors.containsKey(n)) {
            factors.put(n, 1 + factors.get(n));
        } else {
            factors.put(n, 1);
        }

        return factors;
    }

    /**
     * @see java.util.List#get(int)
     * @param n
     * @return nth prime (starting with 2)
     * @throws IllegalArgumentException
     *             if negative number
     */
    public Integer get(int n) {
        assert n >= 0;

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
     */
    public int indexOf(Integer n) {
        if (!isPrime(n)) {
            return -1;
        }

        // easy way
        if (n < primecache.get(primecache.size() - 1)) {
            return primecache.indexOf(n);
        }

        // hard way
        int index = 0;
        for (int i : sieve) {
            if (i == n) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Convert a factorization to a human-friendly string. The format is a
     * comma-delimited list where each element is either a prime number p (as
     * "p"), or the nth power of a prime number as "p^n".
     * 
     * @param factors
     *            factorization
     * @return string representation of factorization.
     * @throws IllegalArgumentException
     *             if negative number
     */
    public String toString(Map<Integer, Integer> factors) {
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

    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Integer> iterator() {
        return new EratosthenesListIterator();
    }

    public ListIterator<Integer> listIterator() {
        return new EratosthenesListIterator();
    }

    /**
     * List iterator.
     * 
     * @author Bear Giles <bgiles@coyotesong.com>
     */
    class EratosthenesListIterator extends AbstractListIterator<Integer> {
        int offset = 2;

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

    public static void main(String[] args) {
        for (int n = 2; n < 100; n++) {
            System.out.printf("%d  %s%n", n, SIEVE.toString(SIEVE.factorize(n)));
        }
    }
}
