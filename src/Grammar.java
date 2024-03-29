
public class Grammar {
	
	public static boolean getGrammar(String[] sentence,String [] tags, double[] probs){
		
		boolean returnValue = true;	
			
		returnValue = returnValue & verbsInSuccession(sentence, tags, probs);
		//returnValue = returnValue & CheckProbabilities(sentence, tags, probs);
		returnValue = returnValue & CheckSVO(sentence, tags, probs);
		returnValue = returnValue & CheckRepeat(sentence, tags, probs);
		returnValue = returnValue & RemoveEndLine(sentence, tags, probs);
		//returnValue = returnValue & MyHimHerIts(sentence, tags, probs);
		returnValue = returnValue & compoundInSuccession(sentence, tags, probs);
		//returnValue = returnValue & RemoveDotAndComma(sentence);
		//returnValue = returnValue & MultipleIts(sentence);
	 return returnValue;
}
	
	/**
	 * Removes dots and commas as a prediction
	 * @param sentence
	 * @return
	 */
	private static boolean RemoveDotAndComma(String[] sentence)
	{
		if(sentence[sentence.length - 1].equals(".") || sentence[sentence.length - 1].equals(","))
			return false;
		
		return true;
		
	}
	
	/**
	 * Check that an a sentence cannot contain it two times with a word between them, such as "it and it".
	 * @param sentence
	 * @return
	 */
	private static boolean MultipleIts(String[] sentence)
	{
		if(sentence.length >= 3)
		{
			if(sentence[sentence.length - 3].equals("it") && sentence[sentence.length - 1].equals("it"))
				return false;
		}
		return true;
	}
	
	/**
	 * Removes the prediction of the end line
	 * @param sentence
	 * @param tags
	 * @param probs
	 * @return
	 */
	private static boolean RemoveEndLine(String[] sentence, String[] tags, double[] probs)
	{
		if(sentence[sentence.length - 1].equals("</s>"))
			return false;
		
		return true;
	}
	
	/**
	 * Check that the same word cannot repeat itself twice
	 * @param sentence
	 * @param tags
	 * @param probs
	 * @return
	 */
	private static boolean CheckRepeat(String[] sentence, String[] tags, double[] probs)
	{
		
		if(sentence.length >= 2)
			if(sentence[sentence.length - 2].equals(sentence[sentence.length - 1]))
				return false;
		
		return true;
	}
	
	/**
	 * Not used at this time
	 * @param sentence
	 * @param tags
	 * @param probs
	 * @return
	 */
	public static boolean MyHimHerIts(String[] sentence, String[] tags, double[] probs)
	{
		if(tags[tags.length - 2].equals("PRP$") && !tags[tags.length - 1].startsWith("JJ") && !tags[tags.length - 1].startsWith("NN"))
			return false;
		return true;
	}
	
	/**
	 * Check that a verb or similar should come after a PRP tag
	 * @param sentence
	 * @param tags
	 * @param probs
	 * @return
	 */
	private static boolean CheckSVO(String[] sentence, String[] tags, double[] probs)
	{
		if(sentence.length > 2)
			return true;
		
		//!tags[tags.length - 1].equals("TO")
		if(tags[tags.length - 2].equals("PRP") && (tags[tags.length - 1].charAt(0) != 'V' && !tags[tags.length - 1].equals("MD") && !tags[tags.length - 1].equals("RB") && !tags[tags.length - 1].equals("CC")))
			return false;
		
		return true;
	}
	
	/**
	 * Not used
	 * @param sentence
	 * @param tags
	 * @param probs
	 * @return
	 */
	private static boolean CheckProbabilities(String[] sentence, String[] tags, double[] probs)
	{
	if(probs[probs.length - 1] < 0.1)
			return false;
		if(probs.length > 2)
			if(probs[probs.length - 2] < 0.1)
				return false;
		return true;
	}
	
	/**
	 * Check that two verbs cannot come after each other
	 * @param sentance
	 * @param tags
	 * @param probs
	 * @return
	 */
	private static boolean verbsInSuccession(String[] sentance, String[] tags, double[] probs)
	{
		if(tags[tags.length -2].charAt(0) == 'V' && tags[tags.length - 1].charAt(0) == 'V')
			return false;
		
		return true;
	}
	
	/**
	 * Check that two compounds should not come after eachother
	 * @param sentance
	 * @param tags
	 * @param probs
	 * @return
	 */
	private static boolean compoundInSuccession(String[] sentance, String[] tags, double[] probs){
		
		if((tags[tags.length -1].charAt(0) == 'C' && tags[tags.length - 1].charAt(1) == 'C') && 
				(tags[tags.length -2].charAt(0) == 'C' && tags[tags.length - 2].charAt(1) == 'C')){
			return false;
		}
		return true;
	}
	
	private static boolean hasCompound(String[] sentence, String[] tags){
		
		for(int i=0;i < tags.length; i++){
			
			if(tags[i].charAt(0) == 'C' && tags[i].charAt(1) == 'C' ){
				return true;			
			}
		}	
		return false;
	}
	
}
