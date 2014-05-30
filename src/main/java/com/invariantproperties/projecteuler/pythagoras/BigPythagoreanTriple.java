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
public class BigPythagoreanTriple {
    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);
    private static final BigPythagoreanTriple START = new BigPythagoreanTriple(2,
            1);
    private BigInteger m;
    private BigInteger n;
    private BigInteger a;
    private BigInteger b;
    private BigInteger c;
    private BigInteger d;

    public BigPythagoreanTriple(long m, long n) {
        this(BigInteger.valueOf(m), BigInteger.valueOf(n));
    }

    public BigPythagoreanTriple(BigInteger m, BigInteger n) {
        if (m.compareTo(TWO) < 0) {
            throw new IllegalArgumentException("m must be greater than one");
        }

        if (!((ZERO.compareTo(n) < 0) && (n.compareTo(m) < 0))) {
            throw new IllegalArgumentException(
                "n must be between zero and m exclusive");
        }

        this.m = m;
        this.n = n;

        BigInteger m2 = m.pow(2);
        BigInteger n2 = n.pow(2);
        this.a = m2.subtract(n2);
        this.c = m2.add(n2);
        this.b = TWO.multiply(this.m).multiply(this.n);
        this.d = a.multiply(this.m).multiply(this.n);
    }

    /**
     * Get the Euclidean index m.
     *
     * @return
     */
    public BigInteger getM() {
        return m;
    }

    /**
     * Get the Euclidean index n.
     *
     * @return
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * Get the length of the odd leg.
     *
     * @return
     */
    public BigInteger getA() {
        return a;
    }

    /**
     * Get the length of the even leg.
     */
    public BigInteger getB() {
        return b;
    }

    /**
     * Get the hypothenus.
     *
     * @return
     */
    public BigInteger getC() {
        return c;
    }

    /**
     * Get the perimeter of the triangle. That is, a + b + c.
     *
     * @return
     */
    public BigInteger getPerimeter() {
        return a.add(b).add(c);
    }

    /**
     * Get the area of the triangle. That is, ab/2.
     *
     * @return
     */
    public BigInteger getArea() {
        return a.multiply(b).divide(TWO);
    }

    /**
     * Get the radius of the incircle for the triangle.
     *
     * @return
     */
    public BigInteger getInRadius() {
        return n.multiply(m.subtract(n));
    }

    /**
     * Get the value Y that solves the elliptic equation y^2 = x^3 - Dx
     *
     * @return
     */
    public BigInteger getY() {
        return (d.pow(2)).divide(n.pow(2));
    }

    /**
     * Get the value X that solves the elliptic equation y^2 = x^3 - Dx
     *
     * @return
     */
    public BigInteger getX() {
        return d.multiply(m).divide(n);
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
     *
     * @param idx
     * @return
     */
    public static BigPythagoreanTriple getInstance(int idx) {
        if (idx < 0) {
            throw new IllegalArgumentException(
                "idx must be zero or positive number");
        }

        if (0 == idx) {
            return START;
        }

        return getInstance(BigInteger.valueOf(idx));
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
    public static BigPythagoreanTriple getInstance(BigInteger idx) {
        Stack<Integer> stack = new Stack<Integer>();

        while (idx.compareTo(ZERO) > 0) {
            BigInteger i = idx.subtract(ONE);
            stack.push(i.remainder(THREE).intValue());
            idx = i.divide(THREE);
        }

        BigInteger m = TWO;
        BigInteger n = ONE;

        while (!stack.isEmpty()) {
            BigInteger t = m;

            switch (stack.pop()) {
            case 0: // U
                m = TWO.multiply(m).subtract(n);
                n = t;

                break;

            case 1: // A
                m = TWO.multiply(m).add(n);
                n = t;

                break;

            case 2: // D
                m = m.add(TWO.multiply(n));

                break;
            }

            idx = idx.subtract(ONE).divide(THREE);
        }

        return new BigPythagoreanTriple(m, n);
    }
}
