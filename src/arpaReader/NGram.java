package arpaReader;

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
}
