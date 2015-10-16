import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

	public static boolean useGrammar = true;
	public static boolean useContext = true;
	
	public static void main(String[] args) {
		
		//Default to kneser ney smoothed N-grams
		String path = "kn.model.arpa";
		//Choose using command line argument
		if(args.length > 0){
			switch (args[0]){
				case "kn": 
					path = "kn.model.arpa";
					break;
				case "abs": 
					path = "abs.model.arpa";
					break;
				case "ml": 
					path = "ml.model.arpa";
					break;
				case "knlc": 
					path = "kn.model.lowercase.arpa";
					break;
				case "abslc": 
					path = "abs.model.lowercase.arpa";
					break;
				case "mllc": 
					path = "ml.model.lowercase.arpa";
					break;
				default:
					System.out.println("Invalid first argument, arpa file!");
					System.exit(0);
			}
		}
		// Second argument is whether or not to use grammar
		if(args.length > 1){
			switch (args[1]){
			case "grammar": 
				useGrammar = true;
				break;
			case "nogrammar": 
				useGrammar = false;
				break;
			default:
				System.out.println("Invalid second argument, choose grammar or nogrammar!");
				System.exit(0);
			}
		}
		// Second argument is whether or not to use grammar
		if(args.length > 2){
			switch (args[2]){
			case "context": 
				useContext = true;
				break;
			case "nocontext": 
				useContext = false;
				break;
			default:
				System.out.println("Invalid third argument, choose context or nocontext!");
				System.exit(0);
			}
		}
		
		System.out.println("Using ARPA file: "+path+" , Grammar:"+useGrammar+" , Context:"+useContext );
		Path p = FileSystems.getDefault().getPath(path);
		
		System.out.println("Creating datastructure for N-grams...");
		//Send path to ARPA-file and POS
		NGrams grams = new NGrams(p, "en-pos-maxent.bin");
		
		System.out.println("Starting user interface...\n");
		Scanner in;
		
		while(true){
			in = new Scanner(System.in);
			System.out.print("Number of predictions [1,2,...]: ");
			int numOfPred = in.nextInt();
			System.out.print("Maximum number of N-grams [1,2,...]: ");
			int maxNgSize = in.nextInt();
			// Line below fixes something weird with nextLine(), #java quick fix...
			in = new Scanner(System.in);
			System.out.print("Write your sentence: ");
			String sentence = in.nextLine();
			
			while(true){
				System.out.println("\nGetting prediction for sentence: "+sentence);
				NGram[] result;
				if(sentence.endsWith("*")){
					//Get prediction with some letter given
					result = grams.GetPrediction(sentence.substring(0, sentence.length()-1), numOfPred, maxNgSize);
				}
				else{
					//Get prediction for whole word
					result = grams.GetPredictionNextWord(sentence, numOfPred, maxNgSize);
				}
				// Print all predictions
				for(int i=0; i<result.length; i++){
					String currentWord = result[i].GetLastWord();
					System.out.print(Integer.toString(i)+":"+currentWord+"  ");
				}
				System.out.println("\nChoose a word (-1 if other word, -2 to restart, -3 to exit program)");
				int input = in.nextInt();
				
				if(input == -1 || input < -3  || input > result.length-1){
					//Enter a new letter or word and add it to the sentence
					if(sentence.endsWith("*")){
						sentence = sentence.substring(0, sentence.length()-1);
						System.out.print(sentence);
						String word = in.next();
						sentence += (word);
					}
					else{
						sentence += " ";
						System.out.print(sentence);
						String word = in.next();
						sentence += (word);
					}
					
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
					if(sentence.endsWith("*")){
						int k=0;
						for(int j=sentence.length()-1; j>=0; j--){
							if(sentence.charAt(j)==' '){
								k++;
								break;
							}
							else{
								k++;
							}
						}
						sentence = sentence.substring(0, sentence.length()-k);
					}
					sentence += (" " + result[input].GetLastWord());
				}
			}
		}
	}

}
