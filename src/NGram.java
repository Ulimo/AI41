public class NGram implements Comparable<NGram> {

	private String[] Words; //Words that the n-gram contains
	private double Probability; //The probability of the n-gram
	
	private double Backoff; //The back-off of the n-gram
	
	public NGram(String[] words, double propbability, double Backoff)
	{
		Words = words;
		Probability = propbability;
		this.Backoff = Backoff;
	}
	
	/**
	 * Returns the n-gram size of the word
	 * @return
	 */
	public int NGramSize()
	{
		return Words.length;
	}
	
	/**
	 * Returns the last word in the n-gram
	 * @return
	 */
	public String GetLastWord()
	{
		return Words[Words.length - 1];
	}
	
	/**
	 * Get all words in the n-gram
	 * @return
	 */
	public String[] GetWords()
	{
		return Words;
	}
	
	/**
	 * Return a certain word in the n-gram at location i
	 * @param i
	 * @return
	 */
	public String GetWord(int i)
	{
		return Words[i];
	}
	
	/**
	 * Returns the probability of the n-gram
	 * @return
	 */
	public double GetProbability()
	{
		return Probability;
	}
	
	/**
	 * Returns the backoff
	 * @return
	 */
	public double GetBackoff()
	{
		return Backoff;
	}

	
	/**
	 * Compare function of two n-grams, used for binary search
	 */
	@Override
	public int compareTo(NGram o) {
		
		int compareValue = 0;
		
		if(o.NGramSize() > Words.length) //If the n-gram we are comparing with is of a larger size, only compare up to this n-gram size
		{
			for(int i = 0; i < Words.length; i++)
			{
				compareValue = Words[i].compareTo(o.GetWord(i));
				if(compareValue != 0)
					return compareValue;
			}
		}
		else //Else the binary search is for predicting a word given a prefix of that word
		{
			for(int i = 0; i < Words.length - 1; i++) //compare up to the last word between the n-grams
			{
				compareValue = Words[i].compareTo(o.GetWord(i));
				if(compareValue != 0)
					return compareValue;
			}
			
			if(compareValue == 0) //If the n-grams are still equal
			{
				int minVal = Words[Words.length - 1].length(); //Gives how many characters we should look for
				
				for(int i = 0; i < minVal; i++) 
				{
					if(o.GetWord(o.NGramSize() - 1).length() == (i)) //If the comparing word is smaller, return
						return 1;
						
					compareValue = Character.compare(Words[Words.length - 1].charAt(i),o.GetWord(Words.length - 1).charAt(i));
					
					if(compareValue != 0)
					{
						return compareValue;
					}
				}
			}
		}

		return compareValue;
	}
}
