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
	
	HashMap<Integer, NGramHandler> handlers; //Hash map which contains handlers which represent each n-gram size with its respective grams
	
	private int LargestNGramSize; //The largest n-gram size available
	
	private POSTaggerME tagger = null; //Reference to OpenNLP pos tagger


	/**
	 * Constructor, creates a new ngram model
	 * @param pathToCorpus The path to the Arpa file with n-grams
	 * @param PathToPOS Path to the POS data for OpenNLP POSTagger
	 */
	public NGrams(Path pathToCorpus, String PathToPOS)
	{
		ReadTrainingData(pathToCorpus); //Read the arpa file
		CreatePosModel(PathToPOS); //Create the POS tagger
	}
	
	/**
	 * Creates a new OpenNLP POS tagger
	 * @param PathToPOS
	 */
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
	
	/**
	 * Read the training data for the n-grams from an arpa file
	 * @param p
	 */
	private void ReadTrainingData(Path p)
	{
		ArpaRead reader = new ArpaRead(p); //Create a new arpa reader which creates n-grams from an arpa file
		handlers = reader.GetNGrams(); //Get the created handlers
		
		
		SortHandlers(); //Sort n-grams for binary search
	}
	
	/**
	 * Finds the location of the last occuring it, and replace it with the first occuring noun in a sentence.
	 * @param words
	 * @return
	 */
	private String[] findSubjectChangeIt(String[] words)
	{
		//If context is disabled, do nothing = return words
		if(!Main.useContext){
			return words;
		}
		
		String[] tags = tagger.tag(words);
		int indexOfIt=-1;
		for(int i=words.length-2; i>=0 && ((words.length-2)-i)<LargestNGramSize; i--){
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
				if(j>0 && (tags[j-1].startsWith("PRP") || tags[j -1 ].startsWith("DT"))){ // 
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
				break;
			}
		}
		return words;
	}
	
	/**
	 * Get a prediction of the word that is currently being written.
	 * @param sentance
	 * @param PredictionCount
	 * @param MaxNgramSize
	 * @return
	 */
	public NGram[] GetPrediction(String sentance, int PredictionCount, int MaxNgramSize)
	{
		//sentance = sentance.toLowerCase();
		String[] words = sentance.split("\\s+");
		
		String[] wordclones = words.clone();
		words = findSubjectChangeIt(words);
		ArrayToLowercase(words);
		ArrayList<NGram> outList = new ArrayList<>();
		
		String[] wordsForGrammar = new String[wordclones.length + 1];
		
		for(int i = 0; i < wordclones.length; i++)
		{
			wordsForGrammar[i] = wordclones[i];
		}
		
		int HighestGram = Math.min(MaxNgramSize, Math.min(words.length, LargestNGramSize));

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
	
	/**
	 * Make all strings in an array to lowercase
	 * @param stringArray
	 */
	private void ArrayToLowercase(String[] stringArray)
	{
		for(int i = 0; i < stringArray.length; i++)
		{
			stringArray[i] = stringArray[i].toLowerCase();
		}
	}
	
	/**
	 * Add an n-gram to a list if if it doesnt exist inside the list, O(n) for each insertion, keep the list short.
	 * @param list
	 * @param toAdd
	 */
	private void AddToList(ArrayList<NGram> list, NGram toAdd)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).GetLastWord().equals(toAdd.GetLastWord()))
				return;
		}
		list.add(toAdd);
	}
	
	/**
	 * Returns an array from a ArrayList
	 * @param list
	 * @return
	 */
	private NGram[] ReturnArray(ArrayList<NGram> list)
	{
		NGram[] arr = new NGram[list.size()];
		
		return list.toArray(arr);
	}
	
	/**
	 * Returns the next word prediction from a sentence.
	 * @param sentance
	 * @param PredictionCount
	 * @param MaxNgramSize
	 * @return
	 */
	public NGram[] GetPredictionNextWord(String sentance, int PredictionCount, int MaxNgramSize)
	{
		//sentance = sentance.toLowerCase();
		String[] words = sentance.split("\\s+");
		
		String[] wordclones = words.clone();
		words = findSubjectChangeIt(words);
		ArrayToLowercase(words);
		ArrayList<NGram> outList = new ArrayList<>();
		
		String[] wordsForGrammar = new String[wordclones.length + 1];
		
		for(int i = 0; i < wordclones.length; i++)
		{
			wordsForGrammar[i] = wordclones[i];
		}
		
		int HighestGram = Math.min(MaxNgramSize, Math.min(words.length + 1, LargestNGramSize));

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
	
	/**
	 * Clean up of an array, remove occurences of null and create a new array with the exact size of the entries.
	 * @param arr
	 * @return
	 */
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
	
	
	/**
	 * Sort the n-grams in each handler to be used for binary search.
	 */
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
