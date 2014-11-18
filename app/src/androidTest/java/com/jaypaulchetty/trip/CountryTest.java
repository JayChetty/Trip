package com.jaypaulchetty.trip;

import junit.framework.TestCase;

/**
 * Created by jay on 18/11/14.
 */
public class CountryTest extends TestCase {
    protected Country fCountry;

    protected void setUp(){
        fCountry = new Country();
    }

    public void testAdd() {
        double result= 3.0 + 2.0;
        assertTrue(result == 5.0);
    }
}
