public class NGram {

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
}
