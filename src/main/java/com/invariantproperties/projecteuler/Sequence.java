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

import java.util.ListIterator;

/**
 * Semi-infinite sequence. That is, a sequence with elements at 0, 1, 2,...
 * infinity, or at least as close to it as you can get with the available
 * resources.
 * 
 * We often want to use a Sequence instead of a Collection since the standard
 * implementations of the latter require precalculation during construction.
 * This probably means we either wasted effort computing values we don't need or
 * we'll come up short and ask for values that don't exist. A Sequence is
 * usually implemented as a "just-in-time" iterator so it only computes what we
 * need (eliminating the first problem) and computes as much as we need
 * (eliminating the second problem).
 * 
 * Sometimes we want a Collection anyway. In this case we can use a
 * {@link com.invariantproperties.projecteuler.SequenceList} - it is a
 * "just-in-time" (JIT) collection that only computes values as needed.
 * Implementations can cache values so the amortized cost of a lookup can be
 * very low. This is a good reason to use a singleton implementation of the
 * common sequences - see {@link com.invariantproperties.projecteuler.Sequences}
 * .
 * 
 * Finally the JIT property of a Sequence allows some surprising things to be
 * provided. E.g., prime numbers. A prime Sequence can use an internal sieve
 * that is transparently regenerated as needed.
 * 
 * Important: the parameter does not need to be a Number.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 * 
 * @param <E>
 */
public interface Sequence<E> extends Iterable<E> {
    /**
     * Get the OEIS sequence number, if defined, for this sequence.
     * 
     * @return
     */
    String getOeisSequenceNumber();

    /**
     * Are the elements of this sequence unique?
     */
    boolean isUnique();

    /**
     * Get the specified element of the sequence.
     * 
     * @param n
     * @return element
     */
    E get(int n);

    /**
     * Get the semi-infinite list iterator. That is, a bidirectional iterator.
     * 
     * @throws UnsupportedOperationException
     *             if this sequence does not support a bidirectional iterator.
     * 
     * @return
     */
    ListIterator<E> listIterator();

    /**
     * Get semi-infinite list iterator starting at given offset.
     */
    ListIterator<E> listIterator(int startIndex);

    /**
     * Get a finite sublist.
     */
    SequenceList<E> subList(int startIndex, int lastIndex);
}
