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
        Country tl = new Country("TopLeft","TL",new ArrayList<String>(Arrays.asList("TM", "ML")));
        Country tm = new Country("TopMiddle","TM",new ArrayList<String>(Arrays.asList("TL", "MM", "TR")));
        Country tr = new Country("TopRight","TR",new ArrayList<String>(Arrays.asList("TM", "MR")));
        Country ml = new Country("MiddleLeft","ML",new ArrayList<String>(Arrays.asList("TL","MM","BL")));
        Country mm = new Country("MiddleMiddle","MM",new ArrayList<String>(Arrays.asList("ML","TM","MR", "BM")));
        Country mr = new Country("MiddleRight","MR",new ArrayList<String>(Arrays.asList("TR", "MM","BR")));
        Country bl = new Country("BottomLeft","BL",new ArrayList<String>(Arrays.asList("ML", "BM")));
        Country bm = new Country("BottomMiddle","BM",new ArrayList<String>(Arrays.asList("BL", "MM", "BR")));
        Country br = new Country("BottomRight","BR",new ArrayList<String>(Arrays.asList("BM", "MR")));

    public void setUpFull() {
        Map<String, ArrayList<Country>> countriesByRegion = new HashMap<String, ArrayList<Country>>();
        ArrayList<Country> world = new ArrayList<Country>();
        world.add(tl);
        world.add(tm);
        world.add(tr);
        world.add(ml);
        world.add(mm);
        world.add(mr);
        world.add(bl);
        world.add(bm);
        world.add(br);

        countriesByRegion.put("World", world);
        TripCreator.setCountries(countriesByRegion);
    }

    public void setUpSingle() {
        Map<String, ArrayList<Country>> countriesByRegion = new HashMap<String, ArrayList<Country>>();
        ArrayList<Country> world = new ArrayList<Country>();
        world.add(tl);
        countriesByRegion.put("World", world);
        TripCreator.setCountries(countriesByRegion);
    }

    public void testCanGetCountryFromCode() {
        setUpFull();
        Country country = TripCreator.get().getCountry("TL", "World");
        assertTrue(country == tl);
    }

    public void testGetsCorrectNumberOfNeighbours(){
        setUpFull();
        Country country = TripCreator.get().getCountry("TL", "World");
        ArrayList<Country> neighbours = TripCreator.get().getNeighbours(country,"World");
        assertTrue(neighbours.size() == 2);
    }
    public void testGetsCorrectNeighboursOfCountry(){
        setUpFull();
        Country country = TripCreator.get().getCountry("TL", "World");
        ArrayList<Country> neighbours = TripCreator.get().getNeighbours(country,"World");
        assertTrue(neighbours.contains(tm));
        assertTrue(neighbours.contains(ml));
    }

    public void testCanCreateSeedRoutes(){
        setUpFull();
        ArrayList<ArrayList<Country>> seed = TripCreator.get().createSeedRoutes(tl);
        assertEquals(1, seed.size());
        assertEquals(1, seed.get(0).size());
        assertEquals(tl, seed.get(0).get(0));
    }

    public void testCanExtendRoute(){
        setUpFull();
        ArrayList<ArrayList<Country>> seedRoutes = TripCreator.get().createSeedRoutes(tl);
        ArrayList<ArrayList<Country>> outRoutes = TripCreator.get().extendRoutes("World",seedRoutes);

        assertEquals(2, outRoutes.size());
        ArrayList<Country> firstRoute = outRoutes.get(0);
        ArrayList<Country> secondRoute = outRoutes.get(1);

        assertEquals(2, firstRoute.size());
        assertTrue(firstRoute.get(0) == tl );
        assertTrue(firstRoute.get(1) == tm || secondRoute.get(1) == ml);

        assertEquals(2, secondRoute.size());
        assertTrue(secondRoute.get(0) == tl );
        assertTrue(secondRoute.get(1) == tm || secondRoute.get(1) == ml);

        assertFalse(firstRoute.get(1) == secondRoute.get(1));
    }


    public void testCanExtendRouteMulipleTimes(){
        setUpFull();
        ArrayList<ArrayList<Country>> seedRoutes = TripCreator.get().createSeedRoutes(tl);
        ArrayList<ArrayList<Country>> outRoutes = TripCreator.get().extendRoutes("World",seedRoutes,2);

        assertEquals(4, outRoutes.size());
        ArrayList<Country> firstRoute = outRoutes.get(0);
        ArrayList<Country> secondRoute = outRoutes.get(1);
        ArrayList<Country> thirdRoute = outRoutes.get(2);
        ArrayList<Country> forthRoute = outRoutes.get(3);
    }

    public void testCanCreateTrip(){
        setUpFull();
        Trip trip = TripCreator.get().createTrip("World", 3);
        assertEquals(3, trip.getRoute(0).size());
    }

    public void testWillNotFailWhenCantExtend(){
        setUpSingle();
        ArrayList<ArrayList<Country>> seedRoutes = TripCreator.get().createSeedRoutes(tl);
        ArrayList<ArrayList<Country>> outRoutes = TripCreator.get().extendRoutes("World",seedRoutes);
        assertEquals(0, outRoutes.size());
    }

}
