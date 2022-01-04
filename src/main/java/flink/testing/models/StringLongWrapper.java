package flink.testing.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class StringLongWrapper implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 154235876570872896L;
	private Long internalLong = null;
	private String internalString = null;
	
	public StringLongWrapper() {}
	
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
