import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class NGrams {
	
	HashMap<Integer, NGramHandler> handlers;
	
	private int LargestNGramSize;
	
	private POSTaggerME tagger = null;

	public NGrams(Path pathToCorpus, String PathToPOS)
	{
		ReadTrainingData(pathToCorpus);
		CreatePosModel(PathToPOS);
	}
	
	private void CreatePosModel(String PathToPOS)
	{
		InputStream modelIn = null;
		try {
		  modelIn = new FileInputStream(PathToPOS);
		  POSModel model = new POSModel(modelIn);
		  tagger = new POSTaggerME(model);
		}
		catch (IOException e) {
		  // Model loading failed, handle the error
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		    	
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
	}
	
	private void ReadTrainingData(Path p)
	{
		ArpaRead reader = new ArpaRead(p);
		handlers = reader.GetNGrams();
		
		
		SortHandlers();
	}
	
	private String[] findSubjectChangeIt(String[] words)
	{
		String[] tags = tagger.tag(words);
		int indexOfIt=-1;
		for(int i=words.length-1; i>=0 && ((words.length-1)-i)<LargestNGramSize; i--){
			if(words[i].equals("it") || words[i].equals("It")){
				indexOfIt=i;
			}
		}
		if(indexOfIt==-1){
			return words;
		}
		for(int j=0; j<words.length-1; j++){
			if(tags[j].startsWith("NN")){
				words[indexOfIt] = words[j];
				if(j>0 && tags[j-1].startsWith("PRP")){
					String[] tmpWords = new String[words.length+1];
					for(int wordsi=0, tmpi=0; wordsi<words.length; wordsi++, tmpi++){
						if(wordsi==(indexOfIt)){
							tmpWords[tmpi] = words[j-1];
							tmpi++;
						}
						tmpWords[tmpi] = words[wordsi];
					}
					words = tmpWords;
				}
			}
		}
		return words;
	}
	
	public NGram[] GetPrediction(String sentance, int PredictionCount, int MaxNgramSize)
	{
		//sentance = sentance.toLowerCase();
		String[] words = sentance.split("\\s+");
		
		words = findSubjectChangeIt(words);

		ArrayList<NGram> outList = new ArrayList<>();
		
		String[] wordsForGrammar = new String[words.length + 1];
		
		for(int i = 0; i < words.length; i++)
		{
			wordsForGrammar[i] = words[i];
		}
		
		int HighestGram = Math.min(MaxNgramSize, Math.min(words.length, LargestNGramSize));
		//Not yet implemented, should look at the last words up to the largest n-gram size (WordCount + 1)
		for(int i = HighestGram; i >= 1; i--)
		{
			NGramHandler handler = handlers.get(i);
			
			int numberOfWordsToSend = i;// - 1;
			
			String[] wordsToSend = new String[numberOfWordsToSend];
			
			int startInSentence = words.length - numberOfWordsToSend;
			for(int x = 0; x < numberOfWordsToSend; x++)
			{
				wordsToSend[x] = words[startInSentence + x];
			}
			
			NGram[] grams = Cleanup(handler.getMostProbableGrams(wordsForGrammar, wordsToSend, PredictionCount, tagger));
			if(grams != null && grams.length > 0)
			{
				//Cleanup
				for(int x = 0; x < grams.length; x++)
				{
					if(outList.size() == PredictionCount)
						return ReturnArray(outList);
					
					AddToList(outList, grams[x]);
					//outList.add(grams[x]);
				}
				if(outList.size() < PredictionCount)
					continue;
				
				return ReturnArray(outList);
			}
		}
		return ReturnArray(outList);	
	}
	
	private void AddToList(ArrayList<NGram> list, NGram toAdd)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).GetLastWord().equals(toAdd.GetLastWord()))
				return;
		}
		list.add(toAdd);
	}
	
	private NGram[] ReturnArray(ArrayList<NGram> list)
	{
		NGram[] arr = new NGram[list.size()];
		
		return list.toArray(arr);
	}
	
	public NGram[] GetPredictionNextWord(String sentance, int PredictionCount, int MaxNgramSize)
	{
		//sentance = sentance.toLowerCase();
		String[] words = sentance.split("\\s+");
		
		words = findSubjectChangeIt(words);

		ArrayList<NGram> outList = new ArrayList<>();
		
		String[] wordsForGrammar = new String[words.length + 1];
		
		for(int i = 0; i < words.length; i++)
		{
			wordsForGrammar[i] = words[i];
		}
		
		int HighestGram = Math.min(MaxNgramSize, Math.min(words.length + 1, LargestNGramSize));
		//Not yet implemented, should look at the last words up to the largest n-gram size (WordCount + 1)
		for(int i = HighestGram; i >= 1; i--)
		{
			NGramHandler handler = handlers.get(i);
			
			int numberOfWordsToSend = i - 1;
			
			String[] wordsToSend = new String[numberOfWordsToSend];
			
			int startInSentence = words.length - numberOfWordsToSend;
			for(int x = 0; x < numberOfWordsToSend; x++)
			{
				wordsToSend[x] = words[startInSentence + x];
			}
			
			NGram[] grams = Cleanup(handler.getMostProbableGrams(wordsForGrammar, wordsToSend, PredictionCount, tagger));
			if(grams != null && grams.length > 0)
			{
				//Cleanup
				for(int x = 0; x < grams.length; x++)
				{
					if(outList.size() == PredictionCount)
						return ReturnArray(outList);
					
					AddToList(outList, grams[x]);
					//outList.add(grams[x]);
				}
				
				if(outList.size() < PredictionCount)
					continue;
				
				return ReturnArray(outList);
			}
		}
		return ReturnArray(outList);	
	}
	
	private NGram[] Cleanup(NGram[] arr)
	{
		int nonNull = 0;
		
		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i] != null)
			{
				nonNull++;
			}
		}
		
		NGram[] cleanedArr = new NGram[nonNull];
		
		for(int i = 0, z = 0; i < arr.length; i++)
		{
			if(arr[i] != null)
			{
				cleanedArr[z++] = arr[i];
			}
			
		}
		return cleanedArr;
	}
	
	
	
	private void SortHandlers()
	{
		int LargestGramSize = 0;
		
		Iterator<Entry<Integer, NGramHandler>> it = handlers.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer, NGramHandler> entry = it.next();
			
			NGramHandler handler = entry.getValue();
			if(handler != null)
			{
				int gramSize = handler.GetGramSize();
				if(gramSize > LargestGramSize)
				{
					LargestGramSize = gramSize;
				}
				
				handler.Sort();
			}
		}
		LargestNGramSize = LargestGramSize;
		
	}
}
