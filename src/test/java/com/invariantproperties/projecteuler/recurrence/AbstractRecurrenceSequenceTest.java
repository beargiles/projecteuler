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

import com.invariantproperties.projecteuler.AbstractSequenceTest;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


/**
 * Abstract base class for recurrence sequences.
 *
 * @author bgiles
 *
 * @param <E>
 */
public abstract class AbstractRecurrenceSequenceTest<E extends Number>
    extends AbstractSequenceTest<E> {
    /**
     * Constructor
     * @param sequence
     */
    protected AbstractRecurrenceSequenceTest(Class<?> clzz)
        throws NoSuchMethodException {
        super(clzz);
    }

    /**
     * Verify toArray(Integer[])
     */
    @Test
    public void verifyToArrayInteger() {
        List<E> l = seq.subList(0, 10);
        Integer[] a = l.toArray(new Integer[0]);
        Assert.assertEquals(l.size(), a.length);

        for (int i = 0; i < l.size(); i++) {
            Assert.assertEquals(l.get(i).intValue(), a[i].intValue());
        }
    }

    /**
     * Verify toArray(Long[])
     */
    @Test
    public void verifyToArrayLong() {
        List<E> l = seq.subList(0, 10);
        Long[] a = l.toArray(new Long[0]);
        Assert.assertEquals(l.size(), a.length);

        for (int i = 0; i < l.size(); i++) {
            Assert.assertEquals(l.get(i).longValue(), a[i].longValue());
        }
    }

    /**
     * Verify toArray(BigInteger[])
     */
    @Test
    public void verifyToArrayBigInteger() {
        List<E> l = seq.subList(0, 10);
        BigInteger[] a = l.toArray(new BigInteger[0]);
        Assert.assertEquals(l.size(), a.length);

        for (int i = 0; i < l.size(); i++) {
            Assert.assertEquals(l.get(i), a[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToArrayBigDecimal() {
        List<E> l = seq.subList(0, 10);
        l.toArray(new BigDecimal[0]);
    }
}
