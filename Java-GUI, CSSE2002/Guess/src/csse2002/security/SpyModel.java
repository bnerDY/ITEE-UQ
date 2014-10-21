package csse2002.security;

import java.util.ArrayList;
import java.util.List;

import csse2002.math.*;

/**
 * Model for the Spy Simulator.
 * 
 * It should keep track of the value of the secret, and the knowledge-state of
 * the spy, and the informants that the spy is yet to meet.
 */
public class SpyModel {

	/**
	 * The heads-bias of the coin used to decide the secret.
	 */
	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final BigFraction SECRET_BIAS = new BigFraction(1, 2);
	private BigFraction knowledgeState;
	private List<TwoCoinChannel> informants;
	private boolean secret;
	/**
	 * Initialises the model for the Spy Simulator.
	 */
	public SpyModel() {
		informants=new ArrayList<TwoCoinChannel>();
		knowledgeState=new BigFraction(1, 2);
		secret=coinFlip(SECRET_BIAS);
	}
	/**
	 * If the informants != null
	 * @return string presentation of meet informants
	 */
	protected String meet(){
		if(informants.isEmpty()){
			return "There is no informant to meet.";
		}else{
			TwoCoinChannel informant=informants.remove(0);
			boolean outcome=coinFlip(informant.getCoinBias(secret));
			knowledgeState=informant.aPosteriori(knowledgeState,  outcome);
			return "Informant says ..." + LINE_SEPARATOR +  
					  "Heads-bias if true: " + informant.getCoinBias(true)
					  + LINE_SEPARATOR +  "Heads-bias if false: " +  
					  informant.getCoinBias(false) + LINE_SEPARATOR +  
					  "Result is ... "  + (outcome ? "heads" : "tails") + "!"; 
		}
	}
	/**
	 * @ensure the bias != null
	 * @return a random boolean value.
	 */
	private boolean coinFlip(BigFraction bias) {
		return Math.random() < bias.getDoubleValue();
	}
	/**
	 * 
	 * @return string representation of get what spy thinks.
	 */
	protected String getWhatSpyThinks(){
		return "Spy thinks ..." + LINE_SEPARATOR
				+ "Secret is true with probability " + knowledgeState;
	}
	/**
	 * @ensure the parameter != null.
	 * @param tmp
	 */
	protected void addInformants(List<TwoCoinChannel> tmp){
		informants.addAll(tmp);
	}
	/**
	 * 
	 * @return a string representation of the spy guess.
	 */
	protected String guess(){
		boolean guess=((knowledgeState.compareTo
				(new BigFraction(1, 2)) >= 0) ? true : false);
		if(secret){
			knowledgeState=BigFraction.ONE; 
		}else{
			knowledgeState=BigFraction.ZERO; 
		}
		return "Spy guesses that secret is " + guess + LINE_SEPARATOR
				+ "and is ... " + (secret == guess ? "correct" : "incorrect")
				+ "!";
	}

}
