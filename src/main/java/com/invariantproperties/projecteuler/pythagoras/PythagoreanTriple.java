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
package com.invariantproperties.projecteuler.pythagoras;

import java.math.BigInteger;

import java.util.Stack;


/**
 * Primative pythagorean triples. There are three sets of values of interest.
 * First is (m,n), the pair of integers that define a Euclidean triple. Second
 * is (a, b, c), the triplet of integers where a^2 + b^2 = c^2. Finally is (x,
 * y, d), the triplet of integers that solve the elliptic equation y^2=x^3-Dx.
 *
 * @author bgiles
 */
public class PythagoreanTriple {
    private static final PythagoreanTriple START = new PythagoreanTriple(2, 1);
    private long m;
    private long n;
    private long a;
    private long b;
    private long c;
    private BigInteger d;

    public PythagoreanTriple(long m, long n) {
        if (m < 2) {
            throw new IllegalArgumentException("m must be greater than one");
        }

        if (!((0 < n) && (n < m))) {
            throw new IllegalArgumentException(
                "n must be between zero and m exclusive");
        }

        this.m = m;
        this.n = n;

        this.a = (m * m) - (n * n);
        this.c = (m * m) + (n * n);
        this.b = 2 * m * n;
        this.d = BigInteger.valueOf(a).multiply(BigInteger.valueOf(m * n));
    }

    /**
     * Get the Euclidean index m.
     *
     * @return
     */
    public long getM() {
        return m;
    }

    /**
     * Get the Euclidean index n.
     *
     * @return
     */
    public long getN() {
        return n;
    }

    /**
     * Get the length of the odd leg.
     *
     * @return
     */
    public long getA() {
        return a;
    }

    /**
     * Get the length of the even leg.
     */
    public long getB() {
        return b;
    }

    /**
     * Get the hypothenus.
     *
     * @return
     */
    public long getC() {
        return c;
    }

    /**
     * Get the perimeter of the triangle. That is, a + b + c.
     *
     * @return
     */
    public long getPerimeter() {
        return a + b + c;
    }

    /**
     * Get the area of the triangle. That is, ab/2.
     *
     * @return
     */
    public long getArea() {
        return (a * b) / 2;
    }

    /**
     * Get the radius of the incircle for the triangle.
     *
     * @return
     */
    public long getInRadius() {
        return n * (m - n);
    }

    /**
     * Get the value Y that solves the elliptic equation y^2 = x^3 - Dx
     *
     * @return
     */
    public BigInteger getY() {
        return (d.pow(2)).divide(BigInteger.valueOf(n).pow(2));
    }

    /**
     * Get the value X that solves the elliptic equation y^2 = x^3 - Dx
     *
     * @return
     */
    public BigInteger getX() {
        return d.multiply(BigInteger.valueOf(m)).divide(BigInteger.valueOf(n));
    }

    /**
     * Get the value D that solves the elliptic equation y^2 = x^3 - Dx
     *
     * @return
     */
    public BigInteger getD() {
        return d.pow(2);
    }

    /**
     * Get specific Pythagorean triple. This will eventually produce all
     * primitive Pythagorean triples.
     * <p>
     * TODO: is there an easy way to get from (m, n) to idx?
     * <p>
     * TODO: cache values?
     *
     * @param idx
     * @return
     */
    public static PythagoreanTriple getInstance(long idx) {
        if (idx < 0) {
            throw new IllegalArgumentException(
                "idx must be zero or positive number");
        }

        if (0 == idx) {
            return START;
        }

        Stack<Integer> stack = new Stack<Integer>();

        while (idx > 0) {
            stack.push((int) ((idx - 1) % 3));
            idx = (idx - 1) / 3;
        }

        long m = 2;
        long n = 1;

        while (!stack.isEmpty()) {
            long t = m;

            switch (stack.pop()) {
            case 0: // U
                m = (2 * m) - n;
                n = t;

                break;

            case 1: // A
                m = (2 * m) + n;
                n = t;

                break;

            case 2: // D
                m += (2 * n);

                break;
            }

            idx = (idx - 1) / 3;
        }

        return new PythagoreanTriple(m, n);
    }
}
