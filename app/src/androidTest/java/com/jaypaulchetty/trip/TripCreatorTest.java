package com.jaypaulchetty.trip;

import android.test.mock.MockContext;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jay on 07/12/14.
 */
public class TripCreatorTest extends TestCase {
    public void setUp() {
        Map<String, ArrayList<Country>> countriesByRegion = new HashMap<String, ArrayList<Country>>();
        ArrayList<Country> world = new ArrayList<Country>();
        world.add(new Country("TopLeft","TL",new ArrayList<String>(Arrays.asList("TM", "ML"))));
        world.add(new Country("TopMiddle","TM",new ArrayList<String>(Arrays.asList("TL", "MM", "TR"))));
        world.add(new Country("TopRight","TR",new ArrayList<String>(Arrays.asList("TM", "MR"))));
        world.add(new Country("MiddleLeft","ML",new ArrayList<String>(Arrays.asList("TL","MM","BL"))));
        world.add(new Country("MiddleMiddle","MM",new ArrayList<String>(Arrays.asList("ML","TM","MR", "BM"))));
        world.add(new Country("MiddleRight","MR",new ArrayList<String>(Arrays.asList("TR", "MM","BR"))));
        world.add(new Country("BottomLeft","BL",new ArrayList<String>(Arrays.asList("ML", "BM"))));
        world.add(new Country("BottomMiddle","BM",new ArrayList<String>(Arrays.asList("BL", "MM", "BR"))));
        world.add(new Country("BottomRight","BR",new ArrayList<String>(Arrays.asList("BM", "MR"))));
        countriesByRegion.put("World", world);
        TripCreator.setCountries(countriesByRegion);
    }

    public void testGetCountryFromCode() {
        Country country = TripCreator.get().getCountry("TL", "World");
        assertTrue(country.getName() == "TopLeft");
    }
}
