package flink.testing.models;

import lombok.Data;

@Data
public class StringLongWrapper {
	
	
	private Long internalLong = null;
	private String internalString = null;
	
	private StringLongWrapper() {}
	
	public static StringLongWrapper build(Long newLong) {
		StringLongWrapper newWrapper = new StringLongWrapper();
		newWrapper.setInternalLong(newLong);
		newWrapper.addString(newLong);
		
		return newWrapper;
	}
	
	public void addString(Long longValue) {
		this.internalString = String.valueOf(longValue);

	}


}
