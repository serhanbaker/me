package test.java.com.serhan.matchingengine;

import main.java.com.serhan.matchingengine.MatchingEngine;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatchingEngineTest {
    @Test
    public void testAddNewOrder() {
        MatchingEngine engine = new MatchingEngine();

        engine.processOrder("1,B,100,10");

        assertTrue(engine.containsKey(100.0));
        assertEquals(1, engine.get(100.0).size());
        assertEquals(10, engine.get(100.0).get(0).getVolume());
    }

    @Test
    public void testMultipleOrdersAtSamePricePointAreInInsertionOrder() {
        MatchingEngine engine = new MatchingEngine();

        engine.processOrder("1,B,100,10");
        engine.processOrder("2,B,100,5");
        engine.processOrder("3,B,100,17");
        engine.processOrder("4,S,101,44");
        engine.processOrder("5,S,101,19");

        assertTrue(engine.containsKey(100.0));
        assertEquals(3, engine.get(100.0).size());
        assertEquals(2, engine.get(101.0).size());
        assertEquals(2, engine.size());

        assertEquals('B', engine.get(100.0).get(0).getSide());
        assertEquals(100.0, engine.get(100.0).get(0).getPrice(), 0.000001);
        assertEquals(10, engine.get(100.0).get(0).getVolume());

        assertEquals('B', engine.get(100.0).get(1).getSide());
        assertEquals(100.0, engine.get(100.0).get(1).getPrice(), 0.000001);
        assertEquals(5, engine.get(100.0).get(1).getVolume());

        assertEquals('B', engine.get(100.0).get(2).getSide());
        assertEquals(100.0, engine.get(100.0).get(2).getPrice(), 0.000001);
        assertEquals(17, engine.get(100.0).get(2).getVolume());

        assertEquals('S', engine.get(101.0).get(0).getSide());
        assertEquals(101.0, engine.get(101.0).get(0).getPrice(), 0.000001);
        assertEquals(44, engine.get(101.0).get(0).getVolume());

        assertEquals('S', engine.get(101.0).get(1).getSide());
        assertEquals(101.0, engine.get(101.0).get(1).getPrice(), 0.000001);
        assertEquals(19, engine.get(101.0).get(1).getVolume());
    }

    @Test
    public void testExecuteFullTrade() {
        MatchingEngine engine = new MatchingEngine();

        engine.processOrder("1,B,100,5");
        engine.processOrder("2,S,100,5");

        assertFalse(engine.containsKey(100.0));
    }

    @Test
    public void testExecutePartialTrade() {
        MatchingEngine engine = new MatchingEngine();

        engine.processOrder("1,B,100,10");
        engine.processOrder("2,S,100,15");

        assertTrue(engine.containsKey(100.0));
        assertEquals(1, engine.get(100.0).size());
        assertEquals('S', engine.get(100.0).get(0).getSide());
        assertEquals(5, engine.get(100.0).get(0).getVolume());
    }

    @Test
    public void testExecuteFullAndPartialTradesOnBothSides() {
        MatchingEngine engine = new MatchingEngine();

        engine.processOrder("1,B,100,100");
        engine.processOrder("2,S,100,100");
        engine.processOrder("88,S,105,3");
        engine.processOrder("89,S,106,1");
        engine.processOrder("90,S,105,4");
        engine.processOrder("91,B,99,9");
        engine.processOrder("3,S,100,100");
        engine.processOrder("4,S,101,100");
        engine.processOrder("5,B,101,277");
        engine.processOrder("9,B,105,8");
        engine.processOrder("92,B,99,3");

        assertEquals(1, engine.get(106.0).size());
        assertEquals(1, engine.get(105.0).size());
        assertEquals(1, engine.get(101.0).size());
        assertEquals(2, engine.get(99.0).size());

        assertFalse(engine.containsKey(100.0));
        assertFalse(engine.containsKey(98.0));

        assertEquals('S', engine.get(106.0).get(0).getSide());
        assertEquals(1, engine.get(106.0).get(0).getVolume());

        assertEquals('B', engine.get(105.0).get(0).getSide());
        assertEquals(1, engine.get(105.0).get(0).getVolume());

        assertEquals('B', engine.get(101.0).get(0).getSide());
        assertEquals(77, engine.get(101.0).get(0).getVolume());

        assertEquals('B', engine.get(99.0).get(0).getSide());
        assertEquals(12, engine.get(99.0).get(0).getVolume() + engine.get(99.0).get(1).getVolume());
    }
}