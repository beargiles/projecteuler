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
 * Cache interface. Three types of caches are supported:
 *
 * <b>IN-MEMORY CACHE.</b> This cache is held entirely in
 * memory. It may use a LRU algorithm to keep the amount of memory
 * required in check.
 *
 * <b>EXISTING DATABASE-BACKED CACHE.</b> This cache is
 * backed by a database (but may use an in-memory cache as well
 * for performance). The database is already initialized. The database
 * may be read-only, or it may allow additional values to be added over
 * time.
 *
 * <b>NEW DATABASE-BACKED CACHE.</b> This is another cache
 * backed by a database but it must be initialized before use. This could
 * be the first use of a new permanent database, or could be a JVM-based
 * database that only exists for the duration of the application.
 *
 * @author bgiles
 *
 * @param <E>
 */
public interface SequenceCache<E> {
    /**
     * Is this a read-only cache?
     */
    boolean isReadOnly();

    /**
     * Set cache read-only.
     */
    void setReadOnly(boolean readOnly);

    /**
     * Is the cache already initialized?
     */
    boolean isInitialized();

    /**
     * Initialize elements
     * @throws IllegalStateException cache has already been initialized.
     */
    boolean initialize(final List<E> elements);

    /**
     * Initialize elements
     * @throws IllegalStateException cache has already been initialized.
     */
    boolean initialize(final Iterator<E> iterator, long count);

    /**
     * Does cache contain this value?
     */

    //boolean contains(Object o);

    /**
     * Get value from cache.
     *
     * @param n
     * @return
     */
    E get(final int n) throws NoSuchElementException;

    /**
     * Put value into the cache.
     *
     * @throws UnsupportedOperationException
     *             if this is readonly cache.
     */
    void put(final int n, final E value);
    
    /**
     * Rese cache to newly-initialized state.
     */
    void reset();
}
