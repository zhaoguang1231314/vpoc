package io.vizit.vpoc.exercise;


import io.vizit.vpoc.common.datatypes.TaxiRide;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RideCleansingTest extends TaxiRideTestBase<TaxiRide> {

    static final Testable JAVA_EXERCISE = () -> RideCleansingExercise.main(new String[]{});

    @Test
    public void testInNYC() throws Exception {
        TaxiRide atPennStation = testRide(-73.9947F, 40.750626F, -73.9947F, 40.750626F);

        TestRideSource source = new TestRideSource(atPennStation);

        assertEquals(Collections.singletonList(atPennStation), results(source));
    }

    @Test
    public void testNotInNYC() throws Exception {
        TaxiRide toThePole = testRide(-73.9947F, 40.750626F, 0, 90);
        TaxiRide fromThePole = testRide(0, 90, -73.9947F, 40.750626F);
        TaxiRide atNorthPole = testRide(0, 90, 0, 90);

        TestRideSource source = new TestRideSource(toThePole, fromThePole, atNorthPole);

        assertEquals(Collections.emptyList(), results(source));
    }

    private TaxiRide testRide(float startLon, float startLat, float endLon, float endLat) {
        return new TaxiRide(1L, true, new DateTime(0), new DateTime(0),
                startLon, startLat, endLon, endLat, (short) 1, 0, 0);
    }

    protected List<?> results(TestRideSource source) throws Exception {
        Testable javaSolution = () -> RideCleansingSolution.main(new String[]{});
        return runApp(source, new TestSink<>(), JAVA_EXERCISE, javaSolution);
    }

}
