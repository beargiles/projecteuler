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

import java.math.BigInteger;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * SequenceList of numeric values.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 * 
 * @param <E>
 */
public class NumericSequenceList<E extends Number> extends AbstractSequenceList<E> {
    public final List<E> elements;
    public final String oeisSequenceNumber;
    public boolean isUnique;

    public NumericSequenceList(List<E> elements, String oeisSequenceNumber, boolean isUnique) {
        this.elements = elements;
        this.oeisSequenceNumber = oeisSequenceNumber;
        this.isUnique = isUnique;
    }

    @Override
    public String getOeisSequenceNumber() {
        return oeisSequenceNumber;
    }

    @Override
    public boolean isUnique() {
        return isUnique;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public E get(int n) {
        return elements.get(n);
    }

    @Override
    public int indexOf(Object o) {
        return elements.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return elements.lastIndexOf(o);
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return elements.containsAll(c);
    }

    @Override
    public final Object[] toArray() {
        return elements.toArray();
    }

    /**
     * Create an array of the specified type. We only support arrays of
     * Integers, Longs and BigIntegers.
     */
    @Override
    public final <T> T[] toArray(T[] a) {
        if (Long.class.equals(a.getClass().getComponentType())) {
            Long[] z = new Long[elements.size()];

            for (int i = 0; i < elements.size(); i++) {
                z[i] = elements.get(i).longValue();
            }

            return (T[]) z;
        }

        if (Integer.class.equals(a.getClass().getComponentType())) {
            Integer[] z = new Integer[elements.size()];

            for (int i = 0; i < elements.size(); i++) {
                z[i] = elements.get(i).intValue();
            }

            return (T[]) z;
        }

        if (BigInteger.class.equals(a.getClass().getComponentType())) {
            if (BigInteger.class.equals(elements.get(0).getClass())) {
                return (T[]) elements.toArray(new BigInteger[0]);
            }

            BigInteger[] z = new BigInteger[elements.size()];

            for (int i = 0; i < elements.size(); i++) {
                z[i] = BigInteger.valueOf(elements.get(i).longValue());
            }

            return (T[]) z;
        }

        throw new IllegalArgumentException("unsupported array type: " + a.getClass().getComponentType().getName());
    }

    @Override
    public ListIterator<E> iterator() {
        return elements.listIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return elements.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return elements.listIterator(index);
    }

    /**
     * Get sublist.
     */
    public NumericSequenceList<E> subList(int startIndex, int lastIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("fromIndex must be non-negative");
        }

        if (!(startIndex < lastIndex)) {
            throw new IllegalArgumentException("fromIndex must be smaller than endIndex");
        }

        return new NumericSequenceList<E>(elements.subList(startIndex, lastIndex), this.getOeisSequenceNumber(),
                this.isUnique());
    }
}
