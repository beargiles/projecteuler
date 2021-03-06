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
package com.invariantproperties.projecteuler.figurate;

import com.invariantproperties.projecteuler.SequenceList;


/**
 * @author bgiles
 *
 */
@com.invariantproperties.projecteuler.annotation.Sequence(oeis = "A00567")
public final class OctagonalNumber extends PolygonalNumber {
    /**
     * @param n
     */
    public OctagonalNumber() {
        super(8);
    }

    /**
     * @param n
     * @param startIndex
     * @param endIndex
     */
    public OctagonalNumber(int startIndex, int endIndex) {
        super(8, startIndex, endIndex);
    }

    /**
     * @see com.invariantproperties.projecteuler.figurate.PolygonalNumber#getOeisSequenceNumber()
     */
    @Override
    public String getOeisSequenceNumber() {
        return "A000567";
    }

    /**
     * @see com.invariantproperties.projecteuler.figurate.PolygonalNumber#subList(int, int)
     */
    @Override
    public SequenceList<Long> subList(int fromIndex, int toIndex) {
        return new OctagonalNumber(fromIndex, toIndex);
    }
}
