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

import com.invariantproperties.projecteuler.AbstractListIterator;
import com.invariantproperties.projecteuler.InMemorySequenceCache;
import com.invariantproperties.projecteuler.SequenceCache;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.ListIterator;


/**
 * Utility classes for Fibonacci sequences.
 *
 * @author bgiles
 */
@com.invariantproperties.projecteuler.annotation.Sequence(oeis = "A000045")
public final class FibonacciNumber extends AbstractRecurrenceNumber<BigInteger> {
    private final BigInteger TWO = BigInteger.valueOf(2);
    private final BigInteger THREE = BigInteger.valueOf(3);

    public FibonacciNumber() {
        super(new InMemorySequenceCache<BigInteger>());
        initialize();
    }

    // FIXME: how to prevent multiple initialization?
    public FibonacciNumber(SequenceCache<BigInteger> cache) {
        super(cache);
        initialize();
    }

    /**
     * Initialize cache.
     */
    void initialize() {
        cache.initialize(iterator(), 100);
    }

    /**
     * Get OEIS Sequence
     */
    @Override
    public String getOeisSequenceNumber() {
        return "A000045";
    }

    /**
     * Are elements unique?
     */
    @Override
    public boolean isUnique() {
        return false;
    }

    /**
     * Get specified Fibonacci number.
     * @param n
     * @return
     */
    @Override
    public BigInteger get(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("index must be non-negative");
        }

        BigInteger value = null;

        synchronized (cache) {
            value = cache.get(n);

            if (value == null) {
                int m = n / 3;

                switch (n % 3) {
                case 0:
                    value = TWO.multiply(get(m).pow(3))
                               .add(THREE.multiply(get(m + 1)).multiply(get(m))
                                         .multiply(get(m - 1)));

                    break;

                case 1:
                    value = get(m + 1).pow(3)
                                .add(THREE.multiply(get(m + 1)
                                                        .multiply(get(m).pow(2))))
                                .subtract(get(m).pow(3));

                    break;

                case 2:
                    value = get(m + 1).pow(3)
                                .add(THREE.multiply(get(m + 1).pow(2)
                                                        .multiply(get(m))))
                                .add(get(m).pow(3));

                    break;
                }

                cache.put(n, value);
            }
        }

        return value;
    }

    /**
         * @see com.coyotesong.projecteuler.recurrence.Sequence#iterator()
         */
    @Override
    public Iterator<BigInteger> iterator() {
        return new FibonacciIterator();
    }

    /**
         * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator()
         */
    @Override
    public ListIterator<BigInteger> listIterator() {
        return new FibonacciIterator();
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator(int)
     */
    @Override
    public ListIterator<BigInteger> listIterator(int fromIndex) {
        return new FibonacciIterator(fromIndex, this);
    }

    /**
     * ListIterator class.
     * @author bgiles
     */
    private static final class FibonacciIterator extends AbstractListIterator<BigInteger> {
        private BigInteger x = BigInteger.ZERO;
        private BigInteger y = BigInteger.ONE;

        public FibonacciIterator() {
        }

        public FibonacciIterator(int startIndex, FibonacciNumber fibonacci) {
            this.idx = startIndex;
            this.x = fibonacci.get(idx);
            this.y = fibonacci.get(idx + 1);
        }

        protected BigInteger getNext() {
            BigInteger t = x;
            x = y;
            y = t.add(x);

            return t;
        }

        protected BigInteger getPrevious() {
            BigInteger t = y;
            y = x;
            x = t.subtract(x);

            return x;
        }
    }
}
