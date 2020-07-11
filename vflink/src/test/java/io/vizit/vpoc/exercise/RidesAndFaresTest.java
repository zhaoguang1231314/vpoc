package io.vizit.vpoc.exercise;

import io.vizit.vpoc.common.datatypes.TaxiFare;
import io.vizit.vpoc.common.datatypes.TaxiRide;
import org.apache.flink.api.java.tuple.Tuple2;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class RidesAndFaresTest extends TaxiRideTestBase<Tuple2<TaxiRide, TaxiFare>> {

    static final Testable JAVA_EXERCISE = () -> RidesAndFaresExercise.main(new String[]{});


    final TaxiRide ride1 = testRide(1);
    final TaxiRide ride2 = testRide(2);
    final TaxiFare fare1 = testFare(1);
    final TaxiFare fare2 = testFare(2);

    @Test
    public void testInOrder() throws Exception {
        TestRideSource rides = new TestRideSource(ride1, ride2);
        TestFareSource fares = new TestFareSource(fare1, fare2);

        List<Tuple2<TaxiRide, TaxiFare>> expected = Arrays.asList(
                Tuple2.of(ride1, fare1),
                Tuple2.of(ride2, fare2));

        assertThat("Join results don't match", results(rides, fares), containsInAnyOrder(expected.toArray()));
    }

    @Test
    public void testOutOfOrder() throws Exception {
        TestRideSource rides = new TestRideSource(ride1, ride2);
        TestFareSource fares = new TestFareSource(fare2, fare1);

        List<Tuple2<TaxiRide, TaxiFare>> expected = Arrays.asList(
                Tuple2.of(ride1, fare1),
                Tuple2.of(ride2, fare2));

        assertThat("Join results don't match", results(rides, fares), containsInAnyOrder(expected.toArray()));
    }

    private TaxiRide testRide(long rideId) {
        return new TaxiRide(rideId, true, new DateTime(0), new DateTime(0),
                0F, 0F, 0F, 0F, (short) 1, 0, rideId);
    }

    private TaxiFare testFare(long rideId) {
        return new TaxiFare(rideId, 0, rideId, new DateTime(0), "", 0F, 0F, 0F);
    }

    protected List<?> results(TestRideSource rides, TestFareSource fares) throws Exception {
        Testable javaSolution = () -> RidesAndFaresSolution.main(new String[]{});
        return runApp(rides, fares, new TestSink<>(), JAVA_EXERCISE, javaSolution);
    }

}