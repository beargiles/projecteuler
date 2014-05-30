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
import com.invariantproperties.projecteuler.Sequences;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.ListIterator;


/**
 * Utility classes for Lucas sequences. Various properties are
 * demonstrated in LucasGeneratorTest.
 *
 * @author bgiles
 */
@com.invariantproperties.projecteuler.annotation.Sequence(oeis = "A000032")
public final class LucasNumber extends AbstractRecurrenceNumber<BigInteger> {
    private static final BigInteger TWO = BigInteger.valueOf(2);

    public LucasNumber() {
        super(new InMemorySequenceCache<BigInteger>());
        initialize();
    }

    public LucasNumber(SequenceCache<BigInteger> cache) {
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
    public String getOeisSequenceNumber() {
        return "A000032";
    }

    /**
     * Get specified Lucas number.
     * @param n
     * @return
     */
    public BigInteger get(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("index must be non-negative");
        }

        BigInteger value = null;

        synchronized (cache) {
            value = cache.get(n);

            if (value == null) {
                value = Sequences.FIBONACCI.get(n + 1)
                                           .add(Sequences.FIBONACCI.get(n - 1));
                cache.put(n, value);
            }
        }

        return value;
    }

    /**
     * Get semi-infinite iterator for sequence.
     */
    public Iterator<BigInteger> iterator() {
        return new LucasIterator();
    }

    /**
     * Get semi-infinite list iterator for sequence.
     */
    public ListIterator<BigInteger> listIterator() {
        return new LucasIterator();
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator(int)
     */
    public ListIterator<BigInteger> listIterator(int startIndex) {
        return new LucasIterator(startIndex, this);
    }

    /**
     * ListIterator class.
     * @author bgiles
     */
    private static final class LucasIterator extends AbstractListIterator<BigInteger> {
        private BigInteger x = TWO;
        private BigInteger y = BigInteger.ONE;

        public LucasIterator() {
        }

        public LucasIterator(int startIndex, LucasNumber lucas) {
            idx = startIndex;
            this.x = lucas.get(idx);
            this.y = lucas.get(idx + 1);
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
