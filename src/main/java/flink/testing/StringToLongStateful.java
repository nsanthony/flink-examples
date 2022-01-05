package flink.testing;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flink.testing.constants.StateConstants;
import flink.testing.models.StringLongWrapper;

public class StringToLongStateful extends RichFlatMapFunction<StringLongWrapper, Long> {
	private static final long serialVersionUID = 8776389351071032372L;
	private static final Logger log = LoggerFactory.getLogger(StringToLongStateful.class);

	private transient MapState<StringLongWrapper, Long> state;
	private int maxIntToKeep = 10;

	private StringToLongStateful() {
	}
	
	public static StringToLongStateful build() {
		return new StringToLongStateful();
	}
	
	public static StringToLongStateful build(int maxIntToKeep) {
		StringToLongStateful instance = build();
		instance.maxIntToKeep = maxIntToKeep;
		return instance;
	}
	
    @Override
    public void open(Configuration config) {
    	
		MapStateDescriptor<StringLongWrapper, Long> descriptor = new MapStateDescriptor<>(
				StateConstants.StringToLong,
				StringLongWrapper.class,
				Long.class);
		
		log.info("This is the state descriptor " + descriptor);
//		StateTtlConfig ttlConfig = StateTtlConfig
//			    .newBuilder(Time.seconds(1000))
//			    .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
//			    .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
//			    .build();
//		descriptor.enableTimeToLive(ttlConfig);
		
		RuntimeContext runtimeContext = getRuntimeContext();
		
		state = runtimeContext.getMapState(descriptor);
		
    }

	@Override
	public void flatMap(StringLongWrapper value, Collector<Long> out) throws Exception {
		if(state.contains(value)) {
			log.info("Already seen string of value " + value);
		}else if(value.getWrappedLong() <= maxIntToKeep) {
			log.info("New value of " + value);
			state.put(value, value.getWrappedLong());
		}
		out.collect(value.getWrappedLong());
	}


}
