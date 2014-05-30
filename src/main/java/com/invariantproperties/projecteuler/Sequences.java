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

import com.invariantproperties.projecteuler.recurrence.FactorialNumber;
import com.invariantproperties.projecteuler.recurrence.FibonacciNumber;
import com.invariantproperties.projecteuler.recurrence.LucasNumber;
import com.invariantproperties.projecteuler.recurrence.PadovanSequence;
import com.invariantproperties.projecteuler.recurrence.PellNumber;
import com.invariantproperties.projecteuler.recurrence.PerrinSequence;

import java.math.BigInteger;
import java.util.ListIterator;


public class Sequences {
    public static final Sequence<BigInteger> FACTORIAL = new FactorialNumber();
    public static final Sequence<BigInteger> FIBONACCI = new FibonacciNumber();
    public static final Sequence<BigInteger> LUCAS = new LucasNumber();
    public static final Sequence<BigInteger> PADOVAN = new PadovanSequence();
    public static final Sequence<BigInteger> PERRIN = new PerrinSequence();
    public static final Sequence<BigInteger> PELL = new PellNumber();

    // Hofstadter sequences?

    // RainbowTableSequenceCache.....

    // Figurate numbers

    /**
     * Get unmodifiable ListIterator.
     */
    public static final <T> ListIterator<T> unmodifiableListIterator(
        ListIterator<T> iterator) {
        return new UnmodifableListIterator<T>(iterator);
    }

    /**
     * Implementation of unmodifiable ListIterator.
     *
     * @author bgiles
     *
     * @param <T>
     */
    private static class UnmodifableListIterator<T> implements ListIterator<T> {
        private final ListIterator<T> parent;

        UnmodifableListIterator(ListIterator<T> parent) {
            this.parent = parent;
        }

        public boolean hasNext() {
            return parent.hasNext();
        }

        public T next() {
            return parent.next();
        }

        public boolean hasPrevious() {
            return parent.hasPrevious();
        }

        public T previous() {
            return parent.previous();
        }

        public int nextIndex() {
            return parent.nextIndex();
        }

        public int previousIndex() {
            return parent.previousIndex();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void add(T x) {
            throw new UnsupportedOperationException();
        }

        public void set(T x) {
            throw new UnsupportedOperationException();
        }
    }
}
