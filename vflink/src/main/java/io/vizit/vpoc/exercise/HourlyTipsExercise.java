package io.vizit.vpoc.exercise;

import io.vizit.vpoc.common.datatypes.TaxiFare;
import io.vizit.vpoc.common.sources.TaxiFareSource;
import io.vizit.vpoc.common.utils.ExerciseBase;
import io.vizit.vpoc.common.utils.MissingSolutionException;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * The "Hourly Tips" exercise of the Flink training in the docs.
 *
 * <p>The task of the exercise is to first calculate the total tips collected by each driver, hour by hour, and
 * then from that stream, find the highest tip total in each hour.
 *
 * <p>Parameters:
 * -input path-to-input-file
 */
public class HourlyTipsExercise extends ExerciseBase {

    /**
     * Main method.
     *
     * <p>Parameters:
     * -input path-to-input-file
     *
     * @throws Exception which occurs during job execution.
     */
    public static void main(String[] args) throws Exception {

        // read parameters
        ParameterTool params = ParameterTool.fromArgs(args);
        final String input = params.get("input", ExerciseBase.PATH_TO_FARE_DATA);

        final int maxEventDelay = 60;       // events are out of order by max 60 seconds
        final int servingSpeedFactor = 600; // events of 10 minutes are served in 1 second

        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(ExerciseBase.parallelism);

        // start the data generator
        DataStream<TaxiFare> fares = env.addSource(fareSourceOrTest(new TaxiFareSource(input, maxEventDelay, servingSpeedFactor)));

//        throw new MissingSolutionException();
        DataStream<Tuple3<Long, Long, Float>> hourlyTips = fares.keyBy((TaxiFare f) -> f.driverId)
                .window(TumblingEventTimeWindows.of(Time.hours(1)))
                .process(new SumTips());

        DataStream<Tuple3<Long, Long, Float>> hourlyMax = hourlyTips
                .keyBy(t -> t.f0)
                .window(TumblingEventTimeWindows.of(Time.hours(1)))
                .maxBy(2);

        printOrTest(hourlyMax);

        // execute the transformation pipeline
        env.execute("Hourly Tips (java)");
    }

    private static class SumTips extends ProcessWindowFunction<TaxiFare, Tuple3<Long, Long, Float>, Long, TimeWindow> {
        @Override
        public void process(Long driverId, Context context, Iterable<TaxiFare> elements, Collector<Tuple3<Long, Long, Float>> out) throws Exception {
            float hourlyTips = 0;
            for (TaxiFare taxiFare : elements) {
                hourlyTips += taxiFare.tip;
            }
            out.collect(Tuple3.of(context.window().getEnd(), driverId, hourlyTips));
        }
    }
}
