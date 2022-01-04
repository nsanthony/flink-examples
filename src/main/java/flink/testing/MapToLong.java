package flink.testing;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flink.testing.constants.StateConstants;

public class MapToLong extends RichMapFunction<String, Long> {
	private static final long serialVersionUID = 8776389351071032372L;
	private static final Logger log = LoggerFactory.getLogger(MapToLong.class);

	private transient MapState<String, Long> state;
	private int maxIntToKeep = 10;

	private MapToLong() {
	}
	
	public static MapToLong build() {
		return new MapToLong();
	}
	
	public static MapToLong build(int maxIntToKeep) {
		MapToLong instance = build();
		instance.maxIntToKeep = maxIntToKeep;
		return instance;
	}
	
    @Override
    public void open(Configuration config) {
		StateTtlConfig ttlConfig = StateTtlConfig
			    .newBuilder(Time.seconds(1000))
			    .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
			    .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
			    .build();
		
		
		MapStateDescriptor<String, Long> descriptor = new MapStateDescriptor<>(
				StateConstants.StringToLong,
				String.class,
				Long.class);
		
		descriptor.enableTimeToLive(ttlConfig);
		
		state = getRuntimeContext().getMapState(descriptor);
    }
	
	@Override
	public Long map(String value) throws Exception {
		long newLongValue = Long.valueOf(value);
		if(state.contains(value)) {
			log.info("Already seen string of value " + value);
		}else if(newLongValue <= maxIntToKeep) {
			log.info("New value of " + value);
			state.put(value, newLongValue);
		}
			
		
		return newLongValue;
	}


}
