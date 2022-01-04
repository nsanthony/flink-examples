package flink.testing;

import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.runtime.state.storage.JobManagerCheckpointStorage;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flink.testing.constants.StateConstants;


public class MyApp {
	private static final Logger log = LoggerFactory.getLogger(MyApp.class);


	public static void main(String[] args) throws Exception {
				
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		/**
		 * Configure the runtime parameters
		 */
		int maxGeneratedNumber = 1000;
		int maxStateSaved = 10;
		
		if(args.length  > 1) {
			maxGeneratedNumber = Integer.valueOf(args[0]);
			log.info("Setting max count up to " + maxGeneratedNumber);
			maxStateSaved = Integer.valueOf(args[1]);
			log.info("Setting the max count to save in state to " + maxStateSaved);
		}else {
			log.info("Using default count up number of %s", maxGeneratedNumber);
		}
		

		/**
		 * Configure the runtime environment. 
		 */
		env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
		env.setStateBackend(new HashMapStateBackend());	
		env.getCheckpointConfig().setCheckpointStorage(new JobManagerCheckpointStorage());
		env.getCheckpointConfig().enableExternalizedCheckpoints(
			    ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
		
		env.setParallelism(2); //creating a 2 parallel job because it is cool. 
		
		/**
		 * Configure DAG. 
		 */
		DataStream<Long> firstStream = env.fromSequence(0, maxGeneratedNumber);
		DataStream<Long> secondStream = env.fromSequence(0, maxGeneratedNumber);
		
		DataStream<Long> stream = firstStream.union(firstStream, secondStream)
				.map(LongToString.build())
				.uid(StateConstants.LongToString)
				.keyBy(value -> value.getInternalLong())
				.flatMap(StringToLongStateful.build(maxStateSaved))
				.uid(StateConstants.StringToLong);

		stream.print();
        env.executeAsync(MyApp.class.getSimpleName());

	}
	
}

