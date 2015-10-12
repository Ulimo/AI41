
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.uima.postag.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		Path p = FileSystems.getDefault().getPath("model.arpa");
		System.out.println("Creating datastructure for N-grams...");
		NGrams grams = new NGrams(p, "en-pos-maxent.bin");
		
		System.out.println("Starting user interface...\n");
		Scanner in;
		
		while(true){
			in = new Scanner(System.in);
			System.out.print("Write your sentence: ");
			String sentence = in.nextLine();
			
			while(true){
				System.out.println("\nGetting prediction for sentence: "+sentence);
				NGram[] result = grams.GetPrediction(sentence, 10, 5);
				
				for(int i=0; i<result.length; i++){
					String currentWord = result[i].GetLastWord();
					System.out.print(Integer.toString(i)+":"+currentWord+"  ");
				}
				System.out.println("\nChoose a word (-1 if other word, -2 to restart, -3 to exit program)");
				int input = in.nextInt();
				
				if(input == -1 || input < -3  || input > result.length-1){
					//Enter a new word and add it
					System.out.println("Write the correct word: ");
					String word = in.next();
					sentence += (" " + word);
				}
				else if(input == -2){
					//Restart
					System.out.println("Restarting with a new sentence...\n");
					break;
				}
				else if(input == -3){
					//Exit program
					in.close();
					System.exit(0);
				}
				else{
					//Add the chosen word
					sentence += (" " + result[input].GetLastWord());
				}
			}
		}
		
		
		//NGram[] result = grams.GetPrediction("as well", 10, 5);

	}

}
