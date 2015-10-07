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
		for(int i = 0; i < Words.length; i++)
		{
			compareValue = Words[i].compareTo(o.GetWord(i));
			if(compareValue != 0)
				return compareValue;
		}
		// TODO Auto-generated method stub
		return compareValue;
	}
}
