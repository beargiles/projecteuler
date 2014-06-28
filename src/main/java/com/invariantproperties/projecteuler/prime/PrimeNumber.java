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

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import com.invariantproperties.projecteuler.AbstractSequenceList;
import com.invariantproperties.projecteuler.SequenceList;

/**
 * The prime numbers.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 */
@com.invariantproperties.projecteuler.annotation.Sequence(oeis = "A000040")
public class PrimeNumber extends AbstractSequenceList<Integer> {
    private final static SieveOfAtkin sieve = SieveOfAtkin.SIEVE;

    /**
     * @see com.invariantproperties.projecteuler.AbstractSequence#getOeisSequenceNumber()
     */
    @Override
    public String getOeisSequenceNumber() {
        return "A000040";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    /**
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Integer n) {
        return sieve.isPrime(n);
    }

    /**
     * @see java.util.List#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object n) {
        return (n instanceof Integer) ? sieve.isPrime((Integer) n) : false;
    }

    /**
     * @see java.util.List#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(Collection<? extends Object> c) {
        boolean results = true;
        for (Object n : c) {
            results = results && sieve.isPrime((Integer) n);
        }
        return results;
    }

    /**
     * @see com.invariantproperties.projecteuler.AbstractSequence#get(int)
     */
    @Override
    public Integer get(int n) {
        Iterator<Integer> iter = sieve.iterator();
        for (int i = 0; i < n; i++) {
            iter.next();
        }
        return iter.next();
    }

    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Integer n) {
        return sieve.indexOf(n);
    }

    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int indexOf(Object n) {
        return (n instanceof Integer) ? sieve.indexOf((Integer) n) : -1;
    }

    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Integer n) {
        return sieve.indexOf(n);
    }

    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object n) {
        return (n instanceof Integer) ? sieve.indexOf((Integer) n) : -1;
    }

    /**
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * @see java.util.List#size()
     */
    public int size() {
        return Integer.MAX_VALUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#toArray()
     */
    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#toArray(java.lang.Object[])
     */
    @Override
    public <T> T[] toArray(T[] arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see com.invariantproperties.projecteuler.Sequence#subList(int, int)
     */
    @Override
    public SequenceList<Integer> subList(int startIndex, int lastIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.invariantproperties.projecteuler.AbstractSequence#iterator()
     */
    @Override
    public Iterator<Integer> iterator() {
        return listIterator();
    }

    /**
     * @see com.invariantproperties.projecteuler.AbstractSequence#listIterator()
     */
    @Override
    public ListIterator<Integer> listIterator() {
        return listIterator(0);
    }

    /**
     * @see com.invariantproperties.projecteuler.AbstractSequence#listIterator(int)
     */
    @Override
    public ListIterator<Integer> listIterator(int startIndex) {
        // TODO Auto-generated method stub
        return null;
    }
}
