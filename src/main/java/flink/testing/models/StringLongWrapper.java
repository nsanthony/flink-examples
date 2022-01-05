package flink.testing.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class StringLongWrapper implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 154235876570872896L;
	public Long wrappedLong = null;
	public String wrappedString = null;
	
	public StringLongWrapper() {}
	
	public static StringLongWrapper build(Long newLong) {
		StringLongWrapper newWrapper = new StringLongWrapper();
		newWrapper.setWrappedLong(newLong);
		newWrapper.addString(newLong);
		
		return newWrapper;
	}
	
	public void addString(Long longValue) {
		this.wrappedString = String.valueOf(longValue);

	}


}
