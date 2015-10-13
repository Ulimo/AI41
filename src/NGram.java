public class NGram implements Comparable<NGram> {

	private String[] Words;
	private double Probability;
	
	private double Backoff;
	
	public NGram(String[] words, double propbability, double Backoff)
	{
		Words = words;
		Probability = propbability;
		this.Backoff = Backoff;
	}
	
	public int NGramSize()
	{
		return Words.length;
	}
	
	public String GetLastWord()
	{
		return Words[Words.length - 1];
	}
	
	public String[] GetWords()
	{
		return Words;
	}
	
	public String GetWord(int i)
	{
		return Words[i];
	}
	
	public double GetProbability()
	{
		return Probability;
	}
	
	public double GetBackoff()
	{
		return Backoff;
	}

	@Override
	public int compareTo(NGram o) {
		
		int compareValue = 0;
		
		if(o.NGramSize() > Words.length)
		{
			for(int i = 0; i < Words.length; i++)
			{
				compareValue = Words[i].compareTo(o.GetWord(i));
				if(compareValue != 0)
					return compareValue;
			}
		}
		else
		{
			for(int i = 0; i < Words.length - 1; i++)
			{
				compareValue = Words[i].compareTo(o.GetWord(i));
				if(compareValue != 0)
					return compareValue;
			}
			
			if(compareValue == 0)
			{
				int minVal = Words[Words.length - 1].length();//o.GetWord(o.NGramSize() - 1).length();//Math.min(Words[Words.length - 1].length(), o.GetWord(Words.length - 1).length());
				
				for(int i = 0; i < minVal; i++)
				{
					if(o.GetWord(o.NGramSize() - 1).length() == (i))
						return 1;
						
					compareValue = Character.compare(Words[Words.length - 1].charAt(i),o.GetWord(Words.length - 1).charAt(i));
					
					if(compareValue != 0)
					{
						return compareValue;
					}
				}
			}
		}
		// TODO Auto-generated method stub
		return compareValue;
	}
}
