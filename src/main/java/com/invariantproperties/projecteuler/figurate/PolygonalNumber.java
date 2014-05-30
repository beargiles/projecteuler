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
package com.invariantproperties.projecteuler.figurate;

import com.invariantproperties.projecteuler.AbstractListIterator;
import com.invariantproperties.projecteuler.AbstractSequenceList;
import com.invariantproperties.projecteuler.NullSequenceCache;
import com.invariantproperties.projecteuler.SequenceCache;
import com.invariantproperties.projecteuler.SequenceList;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * Polynomial numbers. Many figurate numbers are also polynomial numbers.
 *
 * Implementation note: the indices follow mathematical convention and
 * are different from most other sequences. Specifically 'r', the statistical
 * rank, corresponds to 'n' elsewhere and 'n' indicates the number of sides
 * of the polygon.
 *
 * Implementation note: the cache is unused. It can be specified as a
 * mechanism for persisting values to a database.
 *
 * FIXME: iterator with only sum
 *
 * FIXME: size
 *
 * FIXME: toArray methods
 *
 * FIXME: list operator with index
 *
 * @author bgiles
 */
public abstract class PolygonalNumber extends AbstractSequenceList<Long> {
    private long n;
    private int startIndex = 0;
    private int endIndex = Integer.MAX_VALUE / 2;
    private SequenceCache<Long> cache = new NullSequenceCache<Long>();

    /**
     * Constructor taking the number of sides of the polygon.
     * @param n
     */
    public PolygonalNumber(int n) {
        if (n < 2) {
            throw new IllegalArgumentException("value must be 2 or greater");
        }

        this.n = n;
        endIndex = Integer.MAX_VALUE / (n - 2);
    }

    /**
     * Constructor taking the number of sides of the polygon
     * and a starting and ending index.
     */
    public PolygonalNumber(int n, int startIndex, int endIndex) {
        if (n < 2) {
            throw new IllegalArgumentException("n must be 2 or greater");
        }

        int limit = (Integer.MAX_VALUE / (n - 2)) - 1;

        if (!((0 <= startIndex) && (startIndex <= limit))) {
            throw new IllegalArgumentException(
                "startIndex must be between 0 and " + limit + " inclusive");
        }

        if (!((startIndex <= endIndex) && (endIndex <= limit))) {
            throw new IllegalArgumentException(
                "endIndex must be between startIndex and " + limit +
                " inclusive");
        }

        this.n = n;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    /**
     * Get value.
     */
    @Override
    public final Long get(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("value must be zero or greater");
        }

        int x = r + startIndex;
        Long results = Long.valueOf((((long) x) * (((n - 2L) * x) - (n - 4L))) / 2L);

        if (!cache.isReadOnly()) {
            cache.put(r, results);
        }

        return results;
    }

    /**
     * Get value specifying both 'n' and 'r'
     */
    public static final Long get(int np, int r) {
        if (np < 2) {
            throw new IllegalArgumentException("np must be 2 or greater");
        }

        if (r < 0) {
            throw new IllegalArgumentException("r must be zero or greater");
        }

        return Long.valueOf((((long) r) * (((np - 2L) * r) - (np - 4L))) / 2L);
    }

    @Override
    public final boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public final boolean containsAll(Collection<?> c) {
        boolean results = true;

        for (Object o : c) {
            results = contains(o);

            if (!results) {
                break;
            }
        }

        return false;
    }

    @Override
    public final int indexOf(Object o) {
        if (!Long.class.isAssignableFrom(o.getClass())) {
            return -1;
        }

        long x = ((Long) o).longValue();

        if (x < 0) {
            return -1;
        }

        // calculate S^2. It must be a perfect square if x is an n-gonal number.
        long s2 = (8L * (n - 2) * x) + ((n - 4) * (n - 4));
        long s = Math.round(Math.sqrt(s2));

        if (s2 != (s * s)) {
            return -1;
        }

        // calculate tentative rank.
        long r = ((s + n) - 4) / 2 / (n - 2);

        // verify the results.
        if (x != get((int) r).longValue()) {
            System.err.println("unexpected results!");

            return -1;
        }

        // make sure we're in valid range.
        if (!((startIndex <= r) && (r < endIndex))) {
            return -1;
        }

        return (int) r;
    }

    @Override
    public final int lastIndexOf(Object o) {
        return indexOf(o);
    }

    @Override
    public final boolean isEmpty() {
        return size() > 0;
    }

    @Override
    public final int size() {
        return endIndex - startIndex;
    }

    /**
     * Get sublist in derived classes for type-niceness.
     */
    public abstract SequenceList<Long> subList(int fromIndex, int toIndex);

    @Override
    public final Long[] toArray() {
        Long[] a = new Long[size()];

        for (int i = 0; i < (endIndex - startIndex); i++) {
            a[i] = get(startIndex + i);
        }

        return a;
    }

    /**
     * Create array of specified values.
     */
    @Override
    public final <T> T[] toArray(T[] a) {
        if (Integer.class.equals(a.getClass().getComponentType())) {
            Integer[] z = new Integer[size()];

            for (int i = 0; i < size(); i++) {
                z[i] = Integer.valueOf(get(i).intValue());
            }

            return (T[]) z;
        }

        if (Long.class.equals(a.getClass().getComponentType())) {
            Long[] z = new Long[size()];

            for (int i = 0; i < size(); i++) {
                z[i] = get(i);
            }

            return (T[]) z;
        }

        if (BigInteger.class.equals(a.getClass().getComponentType())) {
            BigInteger[] z = new BigInteger[size()];

            for (int i = 0; i < size(); i++) {
                z[i] = BigInteger.valueOf(get(i));
            }

            return (T[]) z;
        }

        throw new IllegalArgumentException("unsupported array type: " +
            a.getClass().getComponentType().getName());
    }

    /**
     * Get iterator.
     */
    @Override
    public Iterator<Long> iterator() {
        return new PolynomialNumberListIterator(n, startIndex, endIndex);
    }

    /**
     * Get list iterator.
     */
    @Override
    public ListIterator<Long> listIterator() {
        return new PolynomialNumberListIterator(n, startIndex, endIndex);
    }

    @Override
    public ListIterator<Long> listIterator(int index) {
        return new PolynomialNumberListIterator(n, index,
            (int) (Integer.MAX_VALUE / (n + 2)));
    }
    
    /**
     * ListIterator class.
     * @author bgiles
     */
    protected static class PolynomialNumberListIterator
        extends AbstractListIterator<Long> {
        private long n;
        private long x;
        private long step;
        private int startIndex;
        private int endIndex;

        public PolynomialNumberListIterator(long n, int startIndex, int endIndex) {
            this.n = n;
            this.x = PolygonalNumber.get((int) n, 0).longValue();
            this.step = (n - 2);
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public boolean hasNext() {
            return (startIndex + idx) < endIndex;
        }

        @Override
        public Long next() {
            if ((startIndex + idx) > endIndex) {
                throw new NoSuchElementException();
            }

            // FIXME do this incrementally
            return PolygonalNumber.get((int) n, startIndex + idx++);
        }

        @Override
        public boolean hasPrevious() {
            return idx > 0;
        }

        @Override
        public Long previous() {
            if (idx <= 0) {
                throw new NoSuchElementException();
            }

            // FIXME do this incrementally
            return PolygonalNumber.get((int) n, startIndex + --idx);
        }

        protected Long getNext() {
            return null;
        }

        protected Long getPrevious() {
            return null;
        }
    }
}
