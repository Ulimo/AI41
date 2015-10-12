
public class Grammar {
	
	public static boolean getGrammar(String[] sentence,String [] tags, double[] probs){
		
	boolean returnValue = true;	
		
	returnValue = returnValue & verbsInSuccession(sentence, tags, probs);
	
	
 return returnValue;
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
