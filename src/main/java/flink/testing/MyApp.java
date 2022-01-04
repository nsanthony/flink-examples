package flink.testing;

import org.apache.flink.api.common.JobID;
import org.apache.flink.core.execution.JobClient;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.runtime.state.storage.JobManagerCheckpointStorage;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyApp {
	private static final Logger log = LoggerFactory.getLogger(MyApp.class);


	public static void main(String[] args) throws Exception {
				
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		/**
		 * Configure the runtim paramenters
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
		env.enableCheckpointing(1000, CheckpointingMode.EXACTLY_ONCE);
		env.setStateBackend(new HashMapStateBackend());
		env.getCheckpointConfig().setCheckpointStorage(new JobManagerCheckpointStorage());
		env.getCheckpointConfig().enableExternalizedCheckpoints(
			    ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
		
		/**
		 * Configure DAG. 
		 */
		DataStream<Long> minusOne = env.fromSequence(0, maxGeneratedNumber)
				.map(MapToString.build())
				.keyBy(value -> value.length())
				.map(MapToLongStateful.build(maxStateSaved));

		minusOne.print();
        env.executeAsync(MyApp.class.getSimpleName());

	}
	
}

