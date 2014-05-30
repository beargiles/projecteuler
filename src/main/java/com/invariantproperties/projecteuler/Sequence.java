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
 * Semi-infinite sequence. That is, a sequence with elements at
 * 0, 1, 2,... infinity, or at least as close to it as you can get with the
 * available resources.
 *
 * @author bgiles
 *
 * @param <E>
 */
public interface Sequence<E> extends Iterable<E> {
    /**
     * Get the OEIS sequence number, if defined, for this sequence.
     * @return
     */
    String getOeisSequenceNumber();
    
    /**
     * Are the elements of this sequence unique?
     */
    boolean isUnique();

    /**
     * Get the specified element of the sequence.
     * @param n
     * @return element
     */
    E get(int n);

    /**
     * Get the semi-infinite list iterator. That is, a bidirectional iterator.
     *
     * @throws UnsupportedOperationException if this sequence does not
     * support a bidirectional iterator.
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
