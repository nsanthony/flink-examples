package flink.testing;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyApp {
	private static final Logger log = LoggerFactory.getLogger(MyApp.class);

	public static void main(String[] args) throws Exception {
				
		RandomStrings randomStrings = RandomStrings.build();
		
		randomStrings.randomWord();
		
		int maxGeneratedNumber = 1000;
		
		if(args.length  > 0) {
			maxGeneratedNumber = Integer.valueOf(args[0]);
			log.info("Setting max count up to %s", maxGeneratedNumber);
		}else {
			log.info("Using default count up number of %s", maxGeneratedNumber);
		}
		
		
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.enableCheckpointing(100, CheckpointingMode.EXACTLY_ONCE);
		
		DataStream<Long> minusOne = env.generateSequence(0, maxGeneratedNumber)
				.map(MapToString.build())
				.keyBy(value -> value.length())
				.map(MapToLong.build());

		minusOne.print();
        
        env.execute("My App");

	}
	
	
	
}

