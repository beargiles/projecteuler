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

import java.util.Iterator;
import java.util.ListIterator;


@com.invariantproperties.projecteuler.annotation.Sequence(oeis = "A000142")
public final class FactorialNumber extends AbstractRecurrenceNumber<BigInteger> {

    public FactorialNumber() {
    	super(new InMemorySequenceCache<BigInteger>());
        initialize();
    }

    public FactorialNumber(SequenceCache<BigInteger> cache) {
        super(cache);
        initialize();
    }

    /**
     * Get OEIS Sequence
     */
    @Override
    public String getOeisSequenceNumber() {
        return "A000142";
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

        BigInteger f = null;

        synchronized (cache) {
            f = cache.get(n);

            if (f == null) {
                FactorialIterator iter = new FactorialIterator();
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
     * Initialize cache.
     */
    void initialize() {
        cache.initialize(iterator(), 10);
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#iterator()
     */
    @Override
    public Iterator<BigInteger> iterator() {
        return new FactorialIterator();
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator()
     */
    @Override
    public ListIterator<BigInteger> listIterator() {
        return new FactorialIterator();
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator(int)
     */
    @Override
    public ListIterator<BigInteger> listIterator(int startIndex) {
        return new FactorialIterator(startIndex, this);
    }

    /**
     * ListIterator class.
     * @author bgiles
     */
    private static final class FactorialIterator extends AbstractListIterator<BigInteger> {
        private BigInteger x = ONE;

        public FactorialIterator() {
        }

        public FactorialIterator(int startIndex, FactorialNumber factorial) {
            idx = startIndex;
            this.x = factorial.get(idx);
        }

        protected BigInteger getNext() {
            // special case for index = 0.
            // remember that index is pre-incremented
            if (idx == 1) {
                x = ONE;
            } else {
                x = x.multiply(BigInteger.valueOf(idx - 1));
            }

            return x;
        }

        protected BigInteger getPrevious() {
            // special case for index = 0.
            if (idx == 0) {
                return ONE;
            }

            BigInteger t = x;
            x = x.divide(BigInteger.valueOf(idx));

            return t;
        }
    }
}
