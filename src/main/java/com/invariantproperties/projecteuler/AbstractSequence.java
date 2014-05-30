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

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Abstract implementation of
 * {@link com.invariantproperties.projecteuler.Sequence}.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 * 
 * @param <E>
 */
public abstract class AbstractSequence<E> implements Sequence<E> {

    /**
     * Get OEIS Sequence
     */
    public abstract String getOeisSequenceNumber();

    /**
     * Are elements unique?
     */
    public boolean isUnique() {
        return true;
    }

    /**
     * Get specified number.
     * 
     * @param n
     * @return
     */
    public abstract E get(int n);

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#iterator()
     */
    public abstract Iterator<E> iterator();

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator()
     */
    public abstract ListIterator<E> listIterator();

    /**
     * @see com.coyotesong.projecteuler.recurrence.Sequence#listIterator(int)
     */
    public abstract ListIterator<E> listIterator(int startIndex);

    public AbstractSequence() {
        super();
    }
}