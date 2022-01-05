package flink.testing;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.runtime.state.storage.JobManagerCheckpointStorage;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flink.testing.constants.JobConfigConstants;


public class MyApp {
	private static final Logger log = LoggerFactory.getLogger(MyApp.class);


	public static void main(String[] args) throws Exception {
				
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		ParameterTool parameter = ParameterTool.fromArgs(args);
		
		/**
		 * Configure the runtime parameters
		 */
		int maxGeneratedNumber = 100000000;
		int maxStateSaved = 10;
		
		maxGeneratedNumber = Integer.valueOf(parameter.get(JobConfigConstants.GENERATION, "100000000"));
		maxStateSaved = Integer.valueOf(parameter.get(JobConfigConstants.GENERATION, "10"));


		/**
		 * Configure the runtime environment. 
		 */
		env.enableCheckpointing(10000, CheckpointingMode.EXACTLY_ONCE);
		env.setStateBackend(new HashMapStateBackend());	
		env.getCheckpointConfig().setCheckpointStorage(new JobManagerCheckpointStorage());
		env.getCheckpointConfig().enableExternalizedCheckpoints(
			    ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
		
		
		/**
		 * Configure DAG. 
		 */
		DataStream<Long> firstStream = env.fromSequence(0, maxGeneratedNumber);
		DataStream<Long> secondStream = env.fromSequence(0, maxGeneratedNumber);
		
		firstStream.union(firstStream, secondStream)
				.map(LongToString.build())
				.keyBy(wrappedLong -> wrappedLong.wrappedLong)
				.flatMap(StringToLongStateful.build(maxStateSaved))
				.addSink(new CustomSinkFunction());


        env.executeAsync(MyApp.class.getSimpleName());

	}
	
}

