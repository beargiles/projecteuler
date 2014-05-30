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
package com.invariantproperties.projecteuler;

import com.invariantproperties.projecteuler.Sequence;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;


public abstract class AbstractSequenceTest<E> {
    protected static final BigInteger ZERO = BigInteger.ZERO;
    protected static final BigInteger ONE = BigInteger.ONE;
    protected static final BigInteger TWO = BigInteger.valueOf(2);
    protected static final BigInteger THREE = BigInteger.valueOf(3);
    protected static final BigInteger FOUR = BigInteger.valueOf(4);
    protected static final BigInteger FIVE = BigInteger.valueOf(5);
    protected static final BigInteger SIX = BigInteger.valueOf(6);
    protected static final BigInteger SEVEN = BigInteger.valueOf(7);
    protected static final BigInteger EIGHT = BigInteger.valueOf(8);
    protected static final BigInteger NINE = BigInteger.valueOf(9);
    protected static final BigInteger TEN = BigInteger.valueOf(10);
    protected static final BigInteger ELEVEN = BigInteger.valueOf(11);
    protected static final BigInteger TWELVE = BigInteger.valueOf(12);
    protected final Constructor<Sequence<E>> ctor;
    protected Sequence<E> seq;

    /**
     * Constructor taking type of object to create for each test. This class must have
     * a no-parameter constructor but that constructor may be private.
     * 
     * @param clzz
     * @throws NoSuchMethodException
     */
    protected AbstractSequenceTest(Class<?> clzz) throws NoSuchMethodException {
    	List<Class<?>> interfaces = new ArrayList<Class<?>>();
    	for (Class<?> c = clzz; c != null; c = c.getSuperclass()) {
    		interfaces.addAll(Arrays.asList(c.getInterfaces()));
    	}
    	if (!interfaces.contains(Sequence.class)) {
            throw new IllegalArgumentException(
                "specified class must implement Sequence.");
        }

        //@SuppressWarnings("unchecked")
        this.ctor = (Constructor<Sequence<E>>) clzz.getDeclaredConstructor();
    }

    /**
     * How many tests to run.
     */
    public abstract int getMaxTests();

    /**
     * Create fresh test object. We need to create one before each test since we use
     * memoitization.
     */
    @Before
    public void createTestObject() {
        try {
            seq = ctor.newInstance();
        } catch (IllegalArgumentException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Verify the implementation by implementing the definition using
     * values provided by the sequence.
     *
     * @return
     */
    @Test
    public abstract void verifyDefinition();

    /**
     * Verify the initial terms of the sequence.  This, combined
     * with verifyDefinition(), will prove correctness via induction
     * so we only need a few terms.
     */
    @Test
    public abstract void verifyInitialTerms();

    /**
     * Common implementation of verifyInitialTerms().
     */
    protected void verifyInitialTerms(List<E> elements) {
        Iterator<E> elementIter = elements.iterator();
        Iterator<E> sequenceIter = seq.iterator();

        while (elementIter.hasNext() && sequenceIter.hasNext()) {
            Assert.assertEquals(elementIter.next(), sequenceIter.next());
        }
    }

    /**
     * Verify elements are unique if indicated. We don't try to
     * verify duplicate values since the duplicates may be out of range.
     */
    @Test
    public void verifyUniqueness() {
        if (seq.isUnique()) {
            Set<E> elements = new HashSet<E>(getMaxTests());

            for (int n = 0; n < getMaxTests(); n++) {
                Assert.assertTrue(elements.add(seq.get(n)));
            }
        }
    }

    /**
     * Verify that get() and the iterator return identical values.
     */
    @Test
    public void verifyIterator() {
        int n = 0;

        for (E x : seq) {
            Assert.assertEquals(seq.get(n), x);

            if (n++ > getMaxTests()) {
                break;
            }
        }
    }

    /**
     * Verify that get() and the list iterator return identical values.
     */
    @Test
    public void verifyListIterator() {
        int n = 0;

        ListIterator<E> iter = seq.listIterator();

        for (n = 0; n < getMaxTests(); n++) {
            Assert.assertEquals(n, iter.nextIndex());
            Assert.assertTrue(iter.hasNext());
            Assert.assertEquals(seq.get(n), iter.next());
        }

        for (n = getMaxTests(); n > 0; n--) {
            Assert.assertEquals(n - 1, iter.previousIndex());
            Assert.assertTrue(iter.hasPrevious());
            Assert.assertEquals(seq.get(n - 1), iter.previous());
        }

        Assert.assertEquals(-1, iter.previousIndex());
        Assert.assertFalse(iter.hasPrevious());
        Assert.assertTrue(iter.hasNext());
    }

    /**
     * Verify that get() and the offset iterator return identical values.
     */
    @Test
    public void verifyOffsetListIterator() {
        int startIndex = 1;

        Iterator<E> iter = seq.listIterator(startIndex);

        for (int n = 0; (n < getMaxTests()) && iter.hasNext(); n++) {
            Assert.assertEquals(seq.get(startIndex + n), iter.next());
        }
    }

    /**
     * Verify the sublist works with no offset.
     */
    @Test
    public void verifySublistNoOffset() {
        List<E> list = seq.subList(0, getMaxTests());
        Assert.assertEquals(getMaxTests(), list.size());

        for (int n = 0; n < list.size(); n++) {
            Assert.assertEquals(seq.get(n), list.get(n));
        }
    }

    /**
     * Verify the sublist works with offset.
     */
    @Test
    public void verifySublistWithOffset() {
        int startIndex = 1;
        List<E> list = seq.subList(startIndex, getMaxTests());
        Assert.assertEquals(getMaxTests() - startIndex, list.size());

        for (int n = 0; n < list.size(); n++) {
            Assert.assertEquals(seq.get(startIndex + n), list.get(n));
        }
    }

    /**
     * Verify toArray()
     */
    @Test
    public void verifyToArray() {
        List<E> list = seq.subList(0, 10);
        @SuppressWarnings("unchecked")
        E[] a = (E[]) list.toArray();
        Assert.assertEquals(list.size(), a.length);

        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), a[i]);
        }
    }

    /**
     * Verify get() IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyGetIllegalArgumentException1() {
        seq.get(-1);
    }

    /**
     * Verify subList() IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifySublistIllegalArgumentException0() {
        seq.subList(-1, 1);
    }

    /**
     * Verify subList() IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifySublistIllegalArgumentException1() {
        seq.subList(2, 1);
    }

    /**
     * Verify subList() is Immutable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void verifySublistImmutableClear() {
        seq.subList(1, 10).clear();
    }

    /**
     * Verify subList() is Immutable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void verifySublistImmutableAdd() {
        seq.subList(1, 10).add(null);
    }

    /**
     * Verify subList() is Immutable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void verifySublistImmutableRemove() {
        seq.subList(1, 10).remove(0);
    }

    /**
     * Verify subList() is Immutable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void verifySublistImmutableSet() {
        seq.subList(1, 10).set(0, null);
    }
}
