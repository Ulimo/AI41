
public class Grammar {
	
	public static boolean getGrammar(String[] sentence,String [] tags, double[] probs){
		
	boolean returnValue = true;	
		
	returnValue = returnValue & verbsInSuccession(sentence, tags, probs);
	returnValue = returnValue & CheckProbabilities(sentence, tags, probs);
	returnValue = returnValue & CheckSVO(sentence, tags, probs);
	
 return returnValue;
}
	
	private static boolean CheckSVO(String[] sentence, String[] tags, double[] probs)
	{
		//if(sentence.length > 2)
		//	return true;
		
		if(tags[tags.length - 2].equals("PRP") && (tags[tags.length - 1].charAt(0) != 'V' && !tags[tags.length - 1].equals("TO") && !tags[tags.length - 1].equals("MD") && !tags[tags.length - 1].equals("RB") && !tags[tags.length - 1].equals("CC")))
			return false;
		
		return true;
	}
	
	private static boolean CheckProbabilities(String[] sentence, String[] tags, double[] probs)
	{
		//if(probs[probs.length - 1] < 0.3)
		//	return false;
		if(probs.length > 2)
			if(probs[probs.length - 2] < 0.3)
				return false;
		return true;
	}
	
	private static boolean verbsInSuccession(String[] sentance, String[] tags, double[] probs)
	{
		if(tags[tags.length -2].charAt(0) == 'V' && tags[tags.length - 1].charAt(0) == 'V')
			return false;
		
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
