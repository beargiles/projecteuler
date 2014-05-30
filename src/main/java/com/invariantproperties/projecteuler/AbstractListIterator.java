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
import java.util.NoSuchElementException;

/**
 * Abstract implementation of
 * {@link com.invariantproperties.projecteuler.ListIterator}.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 * 
 * @param <E>
 */
public abstract class AbstractListIterator<T> implements ListIterator<T> {
    protected int idx = 0;

    protected abstract T getNext();

    protected abstract T getPrevious();

    public boolean hasNext() {
        return true;
    }

    public T next() {
        idx++;

        return getNext();
    }

    public boolean hasPrevious() {
        return idx > 0;
    }

    public T previous() {
        if (idx < 0) {
            throw new NoSuchElementException();
        }

        idx--;

        return getPrevious();
    }

    public int nextIndex() {
        return idx;
    }

    public int previousIndex() {
        return idx - 1;
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
