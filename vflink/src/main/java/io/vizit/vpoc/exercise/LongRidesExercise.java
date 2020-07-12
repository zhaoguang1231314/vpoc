package io.vizit.vpoc.exercise;

import io.vizit.vpoc.common.datatypes.TaxiRide;
import io.vizit.vpoc.common.sources.TaxiRideSource;
import io.vizit.vpoc.common.utils.ExerciseBase;
import io.vizit.vpoc.common.utils.MissingSolutionException;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.joda.time.Hours;

/**
 * The "Long Ride Alerts" exercise of the Flink training in the docs.
 *
 * <p>The goal for this exercise is to emit START events for taxi rides that have not been matched
 * by an END event during the first 2 hours of the ride.
 *
 * <p>Parameters:
 * -input path-to-input-file
 */
public class LongRidesExercise extends ExerciseBase {

    /**
     * Main method.
     *
     * <p>Parameters:
     * -input path-to-input-file
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        ParameterTool params = ParameterTool.fromArgs(args);
        final String input = params.get("input", ExerciseBase.PATH_TO_RIDE_DATA);

        final int maxEventDelay = 60;       // events are out of order by max 60 seconds
        final int servingSpeedFactor = 600; // events of 10 minutes are served in 1 second

        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(ExerciseBase.parallelism);

        // start the data generator
        DataStream<TaxiRide> rides = env.addSource(rideSourceOrTest(new TaxiRideSource(input, maxEventDelay, servingSpeedFactor)));

        DataStream<TaxiRide> longRides = rides
                .keyBy((TaxiRide ride) -> ride.rideId)
                .process(new MatchFunction());

        printOrTest(longRides);

        env.execute("Long Taxi Rides");
    }

    public static class MatchFunction extends KeyedProcessFunction<Long, TaxiRide, TaxiRide> {
        private transient ValueState<TaxiRide> begin;
        private transient ValueState<TaxiRide> end;

        @Override
        public void open(Configuration config) throws Exception {
//            throw new MissingSolutionException();
            begin = getRuntimeContext().getState(new ValueStateDescriptor<TaxiRide>("begin", TaxiRide.class));
            end = getRuntimeContext().getState(new ValueStateDescriptor<TaxiRide>("end", TaxiRide.class));
        }

        @Override
        public void processElement(TaxiRide ride, Context context, Collector<TaxiRide> out) throws Exception {
            TimerService timerService = context.timerService();
            if (ride.isStart) {
                if (end.value() != null) {
                    out.collect(end.value());
                    end.clear();
                    begin.clear();
                    return;
                } else {
                    long timer = ride.startTime.getMillis() + 3600 * 2 * 1000;
                    timerService.registerEventTimeTimer(timer);
                    begin.update(ride);
                }
            } else {
                if (begin.value() != null) {
                    out.collect(ride);
                    begin.clear();
                    end.clear();
                } else {
                    long timer = ride.startTime.getMillis() + 3600 * 2 * 1000;
                    timerService.registerEventTimeTimer(timer);
                    end.update(ride);
                }
            }
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext context, Collector<TaxiRide> out) throws Exception {
            OutputTag<? super TaxiRide> outputTag = new OutputTag<TaxiRide>("no matched") {};
            if (begin.value() != null) {
                context.output(outputTag, begin.value());
            }

            if (end.value() != null) {
                context.output(outputTag, begin.value());
            }
        }
    }
}
