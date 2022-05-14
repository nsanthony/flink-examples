package flink.testing;

import org.apache.flink.api.common.functions.MapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flink.testing.models.StringLongWrapper;
import lombok.Builder;

@Builder
public class LongToString implements MapFunction<Long, StringLongWrapper> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2902681939313193217L;

	private static final Logger log = LoggerFactory.getLogger(MyApp.class);	
	
	@Override
	public StringLongWrapper map(Long value) throws Exception {
		StringLongWrapper wrappedLong = StringLongWrapper.build(value);
		return wrappedLong;
	}

}
