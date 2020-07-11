package io.vizit.vpoc.exercise;

import io.vizit.vpoc.common.datatypes.TaxiFare;
import io.vizit.vpoc.common.datatypes.TaxiRide;
import io.vizit.vpoc.common.utils.ExerciseBase;
import io.vizit.vpoc.common.utils.MissingSolutionException;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.List;

public abstract class TaxiRideTestBase<OUT> {
    public static class TestRideSource extends TestSource<TaxiRide> implements ResultTypeQueryable<TaxiRide> {
        public TestRideSource(Object ... eventsOrWatermarks) {
            this.testStream = eventsOrWatermarks;
        }

        @Override
        long getTimestamp(TaxiRide ride) {
            return ride.getEventTime();
        }

        @Override
        public TypeInformation<TaxiRide> getProducedType() {
            return TypeInformation.of(TaxiRide.class);
        }
    }

    public static class TestFareSource extends TestSource<TaxiFare> implements ResultTypeQueryable<TaxiFare> {
        public TestFareSource(Object ... eventsOrWatermarks) {
            this.testStream = eventsOrWatermarks;
        }

        @Override
        long getTimestamp(TaxiFare fare) {
            return fare.getEventTime();
        }

        @Override
        public TypeInformation<TaxiFare> getProducedType() {
            return TypeInformation.of(TaxiFare.class);
        }
    }

    public static class TestStringSource extends TestSource<String> implements ResultTypeQueryable<String> {
        public TestStringSource(Object ... eventsOrWatermarks) {
            this.testStream = eventsOrWatermarks;
        }

        @Override
        long getTimestamp(String s) {
            return 0L;
        }

        @Override
        public TypeInformation<String> getProducedType() {
            return TypeInformation.of(String.class);
        }
    }

    public static class TestSink<OUT> implements SinkFunction<OUT> {

        // must be static
        public static final List VALUES = new ArrayList<>();

        @Override
        public void invoke(OUT value, Context context) {
            VALUES.add(value);
        }
    }

    public interface Testable {
        void main() throws Exception;
    }

    protected List<OUT> runApp(TestRideSource source, TestSink<OUT> sink, Testable exercise, Testable solution) throws Exception {
        ExerciseBase.rides = source;

        return execute(sink, exercise, solution);
    }

    protected List<OUT> runApp(TestFareSource source, TestSink<OUT> sink, Testable exercise, Testable solution) throws Exception {
        ExerciseBase.fares = source;

        return execute(sink, exercise, solution);
    }

    protected List<OUT> runApp(TestRideSource rides, TestFareSource fares, TestSink<OUT> sink, Testable exercise, Testable solution) throws Exception {
        ExerciseBase.rides = rides;
        ExerciseBase.fares = fares;

        return execute(sink, exercise, solution);
    }

    protected List<OUT> runApp(TestRideSource rides, TestSink<OUT> sink, Testable solution) throws Exception {
        ExerciseBase.rides = rides;

        return execute(sink, solution);
    }

    protected List<OUT> runApp(TestRideSource rides, TestStringSource strings, TestSink<OUT> sink, Testable exercise, Testable solution) throws Exception {
        ExerciseBase.rides = rides;
        ExerciseBase.strings = strings;

        return execute(sink, exercise, solution);
    }

    private List<OUT> execute(TestSink<OUT> sink, Testable exercise, Testable solution) throws Exception {
        sink.VALUES.clear();

        ExerciseBase.out = sink;
        ExerciseBase.parallelism = 1;

        try {
            exercise.main();
        } catch (Exception e) {
            if (ultimateCauseIsMissingSolution(e)) {
                sink.VALUES.clear();
                solution.main();
            } else {
                throw e;
            }
        }

        return sink.VALUES;
    }

    private List<OUT> execute(TestSink<OUT> sink, Testable solution) throws Exception {
        sink.VALUES.clear();

        ExerciseBase.out = sink;
        ExerciseBase.parallelism = 1;

        solution.main();

        return sink.VALUES;
    }

    private boolean ultimateCauseIsMissingSolution(Throwable e) {
        if (e instanceof MissingSolutionException) {
            return true;
        } else if (e.getCause() != null) {
            return ultimateCauseIsMissingSolution(e.getCause());
        } else {
            return false;
        }
    }
}
