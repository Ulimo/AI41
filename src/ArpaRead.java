
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class ArpaRead {

	private int LineNumber = 0;
	
	private HashMap<Integer, NGramHandler> grams = new HashMap<>();
	
	
	public ArpaRead(Path path)
	{
		try {
			List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
			ReadLines(lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, NGramHandler> GetNGrams()
	{
		return grams;
	}
	
	private void ReadLines(List<String> lines)
	{
		ReadHeader(lines); //Read the header data
		ReadGrams(lines); //Read all the ngrams
	}
	
	private void ReadGrams(List<String> lines)
	{
		
		while(LineNumber < lines.size())
		{
			int StartGramLine = LocateLine(lines, "-grams");
			
			if(StartGramLine == -1)
				break;
			
			LineNumber = StartGramLine;
			int nGramSize = Character.getNumericValue(lines.get(StartGramLine).charAt(1));
			
			int numberOfGrams = grams.get(nGramSize).GetNumberOfGrams();
			LineNumber++;
			int endCond = LineNumber + numberOfGrams;
			for(; LineNumber < endCond && LineNumber < lines.size(); LineNumber++) //lines.get(LineNumber).indexOf('\\') == -1
			{
				String current = lines.get(LineNumber);
				String[] splitted = current.split("\\s+");
				
				if(splitted.length >= 2) //Minimum size for a gram is 2 in an arpa
				{
					String[] words = new String[nGramSize];
					
					for(int i = 1; i < (nGramSize + 1); i++)
					{
						words[i - 1] = splitted[i];
					}
					
					double backoff = 0.0;
					
					if((splitted.length > (nGramSize + 1)))
					{
						backoff = Double.parseDouble(splitted[splitted.length - 1]);
					}
					
					grams.get(nGramSize).AddNGram(new NGram(words, Double.parseDouble(splitted[0]), backoff));
				}
			}
			//System.err.println(LineNumber);
		}
	}
	
	private void ReadHeader(List<String> lines)
	{
		
		int headerStart = LocateLine(lines, "\\data\\");
		LineNumber = headerStart + 1;
		
		String s;
		while(((s = lines.get(LineNumber)).indexOf('\\') == -1) && LineNumber < lines.size())
		{
			ReadData(s);
			LineNumber++;
		}
		//LineNumber--;
	}
	
	private void ReadData(String line)
	{
		if(line.contains("ngram"))
		{
			String[] lines = line.split("\\=");
			if(lines.length == 2) //Correct structure ngram x=number
			{
				String[] prefix = lines[0].split("\\s+");
				
				if(prefix.length == 2) //Correct structure
				{
					int ngramNumber = Integer.parseInt(prefix[1]);
					int numberOfGrams = Integer.parseInt(lines[1]);
					
					grams.put(ngramNumber, new NGramHandler(ngramNumber, numberOfGrams));
				}
			}
		}
	}
	
	private int LocateLine(List<String> lines, String line)
	{
		int i = LineNumber;
		for(; i < lines.size(); i++)
		{
			if(lines.get(i).contains(line))
			{
				break;
			}
		}
		
		if(i < lines.size())
			return i;
		
		return -1;
	}
	
	
}
