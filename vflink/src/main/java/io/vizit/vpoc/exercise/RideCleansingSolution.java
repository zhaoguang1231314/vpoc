package io.vizit.vpoc.exercise;

import io.vizit.vpoc.common.datatypes.TaxiRide;
import io.vizit.vpoc.common.sources.TaxiRideSource;
import io.vizit.vpoc.common.utils.ExerciseBase;
import io.vizit.vpoc.common.utils.GeoUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Solution to the "Ride Cleansing" exercise of the Flink training in the docs.
 *
 * <p>The task of the exercise is to filter a data stream of taxi ride records to keep only rides that
 * start and end within New York City. The resulting stream should be printed.
 *
 * <p>Parameters:
 *   -input path-to-input-file
 */
public class RideCleansingSolution extends ExerciseBase {

    /**
     * Main method.
     *
     * <p>Parameters:
     *   -input path-to-input-file
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        ParameterTool params = ParameterTool.fromArgs(args);
        final String input = params.get("input", PATH_TO_RIDE_DATA);

        final int maxEventDelay = 60;       // events are out of order by max 60 seconds
        final int servingSpeedFactor = 600; // events of 10 minutes are served in 1 second

        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(ExerciseBase.parallelism);

        // start the data generator
        DataStream<TaxiRide> rides = env.addSource(rideSourceOrTest(new TaxiRideSource(input, maxEventDelay, servingSpeedFactor)));

        DataStream<TaxiRide> filteredRides = rides
                // keep only those rides and both start and end in NYC
                .filter(new NYCFilter());

        // print the filtered stream
        printOrTest(filteredRides);

        // run the cleansing pipeline
        env.execute("Taxi Ride Cleansing");
    }

    public static class NYCFilter implements FilterFunction<TaxiRide> {
        @Override
        public boolean filter(TaxiRide taxiRide) {
            return GeoUtils.isInNYC(taxiRide.startLon, taxiRide.startLat) &&
                    GeoUtils.isInNYC(taxiRide.endLon, taxiRide.endLat);
        }
    }
}
