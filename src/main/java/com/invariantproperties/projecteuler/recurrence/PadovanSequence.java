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

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.util.Iterator;
import java.util.ListIterator;


/**
 * Utility classes for Padovan sequences (A000931). Related
 * sequences are the Padovan's spiral number (A134816), Tribonacci
 * numbers (A000073) and integer part of the square root of the
 * n-th Fibonacci number.
 *
 * @author bgiles
 */
@com.invariantproperties.projecteuler.annotation.Sequence(oeis = "A000931")
public final class PadovanSequence extends AbstractRecurrenceNumber<BigInteger> {

    public PadovanSequence() {
        super(new InMemorySequenceCache<BigInteger>());
        initialize();
    }

    public PadovanSequence(SequenceCache<BigInteger> cache) {
        super(cache);
        initialize();
    }

    void initialize() {
        cache.initialize(iterator(), 20);
    }

    /**
     * Get OEIS Sequence
     */
    @Override
    public String getOeisSequenceNumber() {
        return "A000931";
    }

    /**
     * Are elements unique?
     */
    @Override
    public boolean isUnique() {
        return false;
    }

    /**
     * Get specified Padovan number.
     *
     * @param n
     * @return
     */
    @Override
    public BigInteger get(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("index must be non-negative");
        }

        BigInteger f = null;

        synchronized (cache) {
            f = cache.get(n);

            if (f == null) {
                PadovanIterator iter = new PadovanIterator();
                int idx;

                while (iter.hasNext() && (iter.nextIndex() < n)) {
                    idx = iter.nextIndex();
                    cache.put(idx, iter.next());
                }

                f = iter.next();
                cache.put(n, f);
            }
        }

        return f;
    }

    /**
     * Get semi-infinite iterator for sequence.
     */
    @Override
    public Iterator<BigInteger> iterator() {
        return new PadovanIterator();
    }

    /**
     * Get semi-infinite list iterator for sequence.
     */
    @Override
    public ListIterator<BigInteger> listIterator() {
        return new PadovanIterator();
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator(int)
     */
    @Override
    public ListIterator<BigInteger> listIterator(int startIndex) {
        return new PadovanIterator(startIndex, this);
    }

    /**
     * ListIterator class.
     *
     * @author bgiles
     */
    private static final class PadovanIterator extends AbstractListIterator<BigInteger> {
        private BigInteger x = ONE;
        private BigInteger y = ZERO;
        private BigInteger z = ZERO;

        public PadovanIterator() {
        }

        public PadovanIterator(int startIndex, PadovanSequence padovan) {
            idx = startIndex;
            this.x = padovan.get(idx);
            this.y = padovan.get(idx + 1);
            this.z = padovan.get(idx + 2);
        }

        protected BigInteger getNext() {
            BigInteger t = x;
            x = y;
            y = z;
            z = x.add(t);

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
