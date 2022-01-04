package flink.testing;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

//@Flogger
public class RandomStrings implements Serializable {
	private static final long serialVersionUID = -4285958946066935230L;


	private static final Logger log = Logger.getLogger(RandomStrings.class.getName());
	
	
	private Random random = null;
	private int stringLength = 12;
	
	//To prevent public construction
	private RandomStrings() {
		this.random = new Random();
	}
	
	
	public static RandomStrings build() {
		return new RandomStrings();
	}
	
	
	public String randomWord(long stringLength) {
		this.stringLength = (int) stringLength;
		return randomWord();
	}
	
	public String randomWord() {
		byte[] array = new byte[stringLength];
	    random.nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
		
	    log.info("Generated string: " + generatedString);
		return generatedString;
	}
	
	
	public Collection<String> wordCollection(int numberOfWords){
		Collection<String> collectionOfWords = new ArrayList<>(); 
		for(int i = 0; i < numberOfWords; i++) {
			collectionOfWords.add(randomWord() + " " + randomWord());
		}
		
		return collectionOfWords;
	}

}
