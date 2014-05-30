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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility class to cache sequence values.
 *
 * @author bgiles
 */
public class InMemorySequenceCache<E> implements SequenceCache<E> {
    private final List<E> staticCache = new ArrayList<E>();
    private final Map<Integer, E> dynamicCache;
    private boolean isReadOnly = false;
    private boolean isInitialized = false;

    /**
     * Default constructor
     */
    public InMemorySequenceCache() {
        this(1000);
    }

    /**
     * Constructor specifying size of permanent and dynamic cache.
     *
     * @param generator
     * @param size dynamic cache size
     */
    public InMemorySequenceCache(final int size) {
        dynamicCache = Collections.synchronizedMap(new LinkedHashMap<Integer, E>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected boolean removeEldestEntry(
                        Map.Entry<Integer, E> entry) {
                        return dynamicCache.size() > size;
                    }
                });
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.SequenceCache#isInitialized()
     */
    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.SequenceCache#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.SequenceCache#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.SequenceCache#initialize(java.util.List)
     */
    @Override
    public synchronized boolean initialize(final List<E> values) {
        if (isInitialized) {
            throw new IllegalStateException("Cache is already initialized");
        }

        staticCache.addAll(values);
        isInitialized = true;

        return true;
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.SequenceCache#initialize(java.util.Iterator, long)
     */
    @Override
    public synchronized boolean initialize(final Iterator<E> iterator,
        long count) {
        if (isInitialized) {
            throw new IllegalStateException("Cache is already initialized");
        }

        for (int i = 0; (i < count) && iterator.hasNext(); i++) {
            staticCache.add(iterator.next());
        }

        isInitialized = true;

        return true;
    }

    /**
      * @see com.coyotesong.projecteuler.recurrence.SequenceCache#get(int)
      */
    @Override
    public synchronized E get(final int n) {
        if (n < staticCache.size()) {
            return staticCache.get(n);
        }

        // is value in larger (dynamic) cache?
        if (dynamicCache.containsKey(n)) {
            return dynamicCache.get(n);
        }

        return null;
    }

    /**
     * @see com.coyotesong.projecteuler.recurrence.SequenceCache#put(int, Object)
     */
    @Override
    public synchronized void put(final int n, final E value) {
        dynamicCache.put(n, value);
    }
    
    /**
     * @see com.coyotesong.projecteuler.recurrence.SequenceCache#reset()
     */
    public void reset() {
    	dynamicCache.clear();
    }
}
