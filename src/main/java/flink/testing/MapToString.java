package flink.testing;

import org.apache.flink.api.common.functions.MapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapToString implements MapFunction<Long, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2902681939313193217L;

	private static final Logger log = LoggerFactory.getLogger(MyApp.class);

	private RandomStrings randomWords = RandomStrings.build();
	
	
	private MapToString() {	}
	
	public static MapToString build() {
		return new MapToString();
	}
	
	@Override
	public String map(Long value) throws Exception {
//		String output = randomWords.randomWord(value);
		String output = String.valueOf(value);
//		log.info(output);
		return output;
	}

}
