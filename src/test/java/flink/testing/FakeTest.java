package flink.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.experimental.CollectSink;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import flink.testing.constants.StateConstants;
import flink.testing.models.StringLongWrapper;
import scala.collection.parallel.ParIterableLike.Collect;

public class FakeTest {

    private static StringToLongStateful statefulMap;

    private transient MapState<StringLongWrapper, Long> state;

    @ClassRule
    public static MiniClusterWithClientResource flinkCluster =
        new MiniClusterWithClientResource(
            new MiniClusterResourceConfiguration.Builder()
                .setNumberSlotsPerTaskManager(2)
                .setNumberTaskManagers(1)
                .build());

    @BeforeAll
    public static void setup(){
        statefulMap = StringToLongStateful.builder()
        .maxIntToKeep(100)
        .build();
    }

    @Test
    public void testLongToString() throws Exception{
        LongToString longToStringMap = LongToString.builder().build();
        Random random = new Random();
        Long randomLong = random.nextLong();

        StringLongWrapper expected = StringLongWrapper.builder()
                .wrappedLong(randomLong)
                .build();

        StringLongWrapper out = longToStringMap.map(randomLong);

        assertEquals(expected, out);
    }

    @Test
    public void testStatefulFunction(){

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);


        LongToString longToStringMap = LongToString.builder().build();

        CollectSink.values.clear();

        CollectSink sink = new CollectSink();


        env.fromElements(1L, 2L, 3L)
            .map(longToStringMap)
            .keyBy(wrappedLong -> wrappedLong.wrappedLong)
            .flatMap(statefulMap)
            .addSink(sink);

        List<Long> outValues = CollectSink.values;

        assertTrue(CollectSink.values.containsAll(Arrays.asList(1L, 2L)));

 
    }

    private static class CollectSink implements SinkFunction<Long> {

        // must be static
        public static final List<Long> values = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void invoke(Long value, SinkFunction.Context context) throws Exception {
            values.add(value);
        }
    }
    
}
