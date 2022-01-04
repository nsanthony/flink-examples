package flink.testing;

import org.apache.flink.api.common.functions.FilterFunction;

public class StillGreaterThanZero implements FilterFunction<String> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 77764426136666534L;


	private StillGreaterThanZero() {}
	
	public static StillGreaterThanZero build() {
		return new StillGreaterThanZero();
	}
	
	
	@Override
	public boolean filter(String value) throws Exception {
	  return (value.equals("false"));
	}

}
