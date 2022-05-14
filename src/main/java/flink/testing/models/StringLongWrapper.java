package flink.testing.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringLongWrapper implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 154235876570872896L;
	public Long wrappedLong;
	@Builder.Default
	public String wrappedString = null;
	
	public String getWrappedString() {
		if(wrappedString == null){
			wrappedString = String.valueOf(wrappedLong);
		}
		return wrappedString;
	}


}
