package flink.testing;

import org.apache.flink.api.common.functions.FilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LessThanZero implements FilterFunction<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4685411875918598639L;
	private static final Logger log = LoggerFactory.getLogger(LessThanZero.class);

	private LessThanZero() { }
	
	public static LessThanZero build() {
		return new LessThanZero();
	}
	
	@Override
	public boolean filter(String value) throws Exception {
		log.info(String.valueOf(value));
	    return true;
	}

}
