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

import static java.math.BigInteger.ZERO;

import java.util.Iterator;
import java.util.ListIterator;


/**
 * Utility classes for Perrin sequences (A001608). Various properties are
 * demonstrated in PerrinGeneratorTest.
 *
 * @author bgiles
 */
@com.invariantproperties.projecteuler.annotation.Sequence(oeis = "A001608")
public final class PerrinSequence extends AbstractRecurrenceNumber<BigInteger> {
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);
    private static final BigInteger FOUR = BigInteger.valueOf(4);
    private static final BigInteger SIX = BigInteger.valueOf(6);
    private static final BigInteger SEVEN = BigInteger.valueOf(7);
    private static final BigInteger NINE = BigInteger.valueOf(9);
    private static final BigInteger V14 = BigInteger.valueOf(14);
    private static final BigInteger V18 = BigInteger.valueOf(18);
    private static final BigInteger V23 = BigInteger.valueOf(23);

    public PerrinSequence() {
        super(new InMemorySequenceCache<BigInteger>());
        initialize();
    }

    public PerrinSequence(SequenceCache<BigInteger> cache) {
        super(cache);
        initialize();
    }

    void initialize() {
        cache.initialize(iterator(), 100);
    }

    /**
     * Get OEIS Sequence
     */
    @Override
    public String getOeisSequenceNumber() {
        return "A001608";
    }

    /**
     * Are elements unique?
     */
    @Override
    public boolean isUnique() {
        return false;
    }

    /**
     * Get specified Perrin number.
     *
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
                int n0 = n / 2;
                BigInteger u = get(n0 - 1);
                BigInteger v = get(n0);
                BigInteger w = get(n0 + 1);

                BigInteger x = null;

                if ((n % 2) == 0) {
                    x = SEVEN.multiply(v.pow(2)).subtract(SIX.multiply(u.pow(2)))
                             .subtract(TWO.multiply(w.pow(2)))
                             .subtract(FOUR.multiply(u).multiply(v))
                             .add(V18.multiply(u).multiply(w))
                             .add(SIX.multiply(v).multiply(w));
                } else {
                    x = NINE.multiply(u.pow(2)).add(v.pow(2))
                            .add(THREE.multiply(w.pow(2)))
                            .add(SIX.multiply(u).multiply(v))
                            .subtract(FOUR.multiply(u).multiply(w))
                            .add(V14.multiply(v).multiply(w));
                }
                assert (BigInteger.ZERO.equals(x.remainder(V23)));

                value = x.divide(V23);
                cache.put(n, value);
            }
        }

        return value;
    }

    /**
     * Get semi-infinite iterator for sequence.
     */
    @Override
    public Iterator<BigInteger> iterator() {
        return new PerrinIterator();
    }

    /**
     * Get semi-infinite list iterator for sequence.
     */
    @Override
    public ListIterator<BigInteger> listIterator() {
        return new PerrinIterator();
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator(int)
     */
    @Override
    public ListIterator<BigInteger> listIterator(int startIndex) {
        return new PerrinIterator(startIndex, this);
    }

    /**
     * ListIterator class. BigInteger.
     *
     * @author bgiles
     */
    private static final class PerrinIterator extends AbstractListIterator<BigInteger> {
        BigInteger x = THREE;
        BigInteger y = ZERO;
        BigInteger z = TWO;

        public PerrinIterator() {
        }

        public PerrinIterator(int startIndex, PerrinSequence perrin) {
            idx = startIndex;
            // FIXME: initialize without 'get'?
            this.x = perrin.get(idx);
            this.y = perrin.get(idx + 1);
            this.z = perrin.get(idx + 2);
        }

        protected BigInteger getNext() {
            BigInteger t = x;
            x = y;
            y = z;
            z = t.add(x);

            return t;
        }

        protected BigInteger getPrevious() {
            BigInteger t = z;
            z = y;
            y = x;
            x = t.subtract(x);

            return x;
        }
    }
}
