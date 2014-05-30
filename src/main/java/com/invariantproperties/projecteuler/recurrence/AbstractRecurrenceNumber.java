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
package com.invariantproperties.projecteuler.recurrence;

import com.invariantproperties.projecteuler.AbstractSequence;
import com.invariantproperties.projecteuler.NumericSequenceList;
import com.invariantproperties.projecteuler.Sequence;
import com.invariantproperties.projecteuler.SequenceCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class AbstractRecurrenceNumber<E extends Number>
    extends AbstractSequence<E> implements Sequence<E> {
    protected final SequenceCache<E> cache;

    protected AbstractRecurrenceNumber(SequenceCache<E> cache) {
    	this.cache = cache;
    }
    
    /**
     * Get sublist.
     */
    public NumericSequenceList<E> subList(int startIndex, int lastIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("fromIndex must be non-negative");
        }

        if (!(startIndex < lastIndex)) {
            throw new IllegalArgumentException(
                "fromIndex must be smaller than endIndex");
        }

        List<E> elements = new ArrayList<E>(lastIndex - startIndex);

        Iterator<E> iter = listIterator(startIndex);

        for (int i = startIndex; (i < lastIndex) && iter.hasNext(); i++) {
            elements.add(iter.next());
        }

        return new NumericSequenceList<E>(elements,
            this.getOeisSequenceNumber(), this.isUnique());
    }
}
