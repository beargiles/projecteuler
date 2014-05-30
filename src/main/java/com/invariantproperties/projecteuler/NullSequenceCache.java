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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Non-caching "cache". This is a convenient method if you need a SequenceCache
 * for some reason but don't need to/want to actually cache the values.
 * 
 * @author Bear Giles <bgiles@coyotesong.com>
 */
public class NullSequenceCache<E> implements SequenceCache<E> {
    /**
     * Is this a read-only cache?
     */
    @Override
    public boolean isReadOnly() {
        return true;
    }

    /**
     * Set cache read-only.
     */
    @Override
    public void setReadOnly(boolean readOnly) {
    }

    /**
     * Is the cache already initialized?
     */
    @Override
    public boolean isInitialized() {
        return true;
    }

    /**
     * Initialize elements
     * 
     * @throws IllegalStateException
     *             cache has already been initialized.
     */
    @Override
    public boolean initialize(final List<E> elements) {
        throw new IllegalStateException("cache is already initialized");
    }

    /**
     * Initialize elements
     * 
     * @throws IllegalStateException
     *             cache has already been initialized.
     */
    @Override
    public boolean initialize(final Iterator<E> iterator, long count) {
        throw new IllegalStateException("cache is already initialized");
    }

    /**
     * Get value from cache.
     * 
     * @param n
     * @return
     */
    @Override
    public E get(final int n) throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    /**
     * Put value into the cache.
     * 
     * @throws UnsupportedOperationException
     *             if this is readonly cache.
     */
    @Override
    public void put(final int n, final E value) {
    }

    /**
     * 
     */
    @Override
    public void reset() {

    }
}
