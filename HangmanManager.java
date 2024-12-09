import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Manages the details of EvilHangman. This class keeps
 * tracks of the possible words from a dictionary during
 * rounds of hangman, based on guesses so far.
 *
 */
public class HangmanManager {

    // instance variables / fields
	ArrayList<String> listOfAllWordsForHangman = new ArrayList<String>();
	ArrayList<String> activeWords;
	ArrayList<Character> lettersGuessed;
	
	boolean debuggingVar;
	int lengthOfWord;
	int totalNumberOfGuessesLeft;
	int currentGuessNumber;
	int numWordsRemaining;
	String currentSecretPattern;
	HangmanDifficulty difficultyOfGame;
		
    /**
     * Create a new HangmanManager from the provided set of words and phrases.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     * @param debugOn true if we should print out debugging to System.out.
     */
    public HangmanManager(Set<String> words, boolean debugOn) {
    	if (words == null || words.size() <= 0) {
    		throw new IllegalArgumentException("Violation of precondition: words must not be null "
    				+ "and words.size() must be greater than 0.");
    	}
    	for (String w : words) {
    		listOfAllWordsForHangman.add(w);
    	}
    	debuggingVar = debugOn;
    }
    

    /**
     * Create a new HangmanManager from the provided set of words and phrases.
     * Debugging is off.
     * pre: words != null, words.size() > 0
     * @param words A set with the words for this instance of Hangman.
     */
    public HangmanManager(Set<String> words) {  
    	if (words == null || words.size() <= 0) {
    		throw new IllegalArgumentException("Violation of precondition: words must not be null "
    				+ "and words.size() must be greater than 0.");
    	}
    	for (String w : words) {
    		listOfAllWordsForHangman.add(w);
    	}
    	debuggingVar = false;
    }


    /**
     * Get the number of words in this HangmanManager of the given length.
     * pre: none
     * @param length The given length to check.
     * @return the number of words in the original Dictionary
     * with the given length
     */
    public int numWords(int length) {
    	int numWordsOfGivenLength = 0;
    	// Checks how many words in the list of all words for this game are equal in length to 
    	// the given length
        for (String w : listOfAllWordsForHangman) {
        	if (w.length() == length) {
        		numWordsOfGivenLength++;
        	}	
        }
        return numWordsOfGivenLength;
    }


    /**
     * Get for a new round of Hangman. Think of a round as a
     * complete game of Hangman.
     * @param wordLen the length of the word to pick this time.
     * numWords(wordLen) > 0
     * @param numGuesses the number of wrong guesses before the
     * player loses the round. numGuesses >= 1
     * @param diff The difficulty for this round.
     */
    public void prepForRound(int wordLen, int numGuesses, HangmanDifficulty diff) {
    	// Resets the word length, total number of wrong guesses left, difficulty of the game,
    	// and the list of characters guessed
    	lengthOfWord = wordLen;
    	totalNumberOfGuessesLeft = numGuesses;
    	difficultyOfGame = diff;
    	currentGuessNumber = 0;
    	lettersGuessed = new ArrayList<Character>(); 
    	activeWords = new ArrayList<String>();

    	// Resets the list of active words by adding only words from the list of all words that are
    	// equal in length to the specified word length
    	for (String w : listOfAllWordsForHangman) {
    		if (w.length() == wordLen) {
    			activeWords.add(w);
    		}	
    	}
    	
    	// Resets the secret pattern to a string of the given length that contains only dash 
    	// characters
    	StringBuilder sp = new StringBuilder();
    	for (int i = 0; i < wordLen; i++) {
    		sp.append('-');
    	}
    	currentSecretPattern = sp.toString();
    }


    /**
     * The number of words still possible (live) based on the guesses so far.
     *  Guesses will eliminate possible words.
     * @return the number of words that are still possibilities based on the
     * original dictionary and the guesses so far.
     */
    public int numWordsCurrent() {
    	// Returns the number of words remaining in the active list of words
    	numWordsRemaining = activeWords.size();
    	return numWordsRemaining;
    }


    /**
     * Get the number of wrong guesses the user has left in
     * this round (game) of Hangman.
     * @return the number of wrong guesses the user has left
     * in this round (game) of Hangman.
     */
    public int getGuessesLeft() {
        return totalNumberOfGuessesLeft;
    }


    /**
     * Return a String that contains the letters the user has guessed
     * so far during this round.
     * The characters in the String are in alphabetical order.
     * The String is in the form [let1, let2, let3, ... letN].
     * For example [a, c, e, s, t, z]
     * @return a String that contains the letters the user
     * has guessed so far during this round.
     */
    public String getGuessesMade() {
    	// Sorts the list containing all of the letters that have been guessed so far,
    	// alphabetically
        Collections.sort(lettersGuessed);
        // Creates and returns a string that contains all of the letters guessed so far in
        // alphabetical order
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < lettersGuessed.size(); i++) {
        	if (i == 0) {
        		s.append(lettersGuessed.get(i));
        	} else {
        		s.append(", " + lettersGuessed.get(i));
        	}
        }
        s.append("]");
        return s.toString();
    }


    /**
     * Check the status of a character.
     * @param guess The characater to check.
     * @return true if guess has been used or guessed this round of Hangman,
     * false otherwise.
     */
    public boolean alreadyGuessed(char guess) {
    	// Checks whether the letter that the user just guessed  was already in the list
    	// containing characters that have already been guessed
    	if (lettersGuessed.contains(guess)) {
    		return true;
    	}
        return false;
    }


    /**
     * Get the current pattern. The pattern contains '-''s for
     * unrevealed (or guessed) characters and the actual character 
     * for "correctly guessed" characters.
     * @return the current pattern.
     */
    public String getPattern() {
        return currentSecretPattern;
    }


    /**
     * Update the game status (pattern, wrong guesses, word list),
     * based on the give guess.
     * @param guess pre: !alreadyGuessed(ch), the current guessed character
     * @return return a tree map with the resulting patterns and the number of
     * words in each of the new patterns.
     * The return value is for testing and debugging purposes.
     */
    public TreeMap<String, Integer> makeGuess(char guess) {
    	if (alreadyGuessed(guess)) {
    		throw new IllegalStateException("This character has already been guessed.");
    	}
    	
    	lettersGuessed.add(guess);
    	currentGuessNumber++;
    	
    	TreeMap<String, ArrayList<String>> patternsWithAssociatedWords = 
    			new TreeMap<String, ArrayList<String>>();
    	
    	// Checks whether the pattern for each word in the active list of words is already 
    	// in the map containing patterns and words that match each pattern
    	for (String word : activeWords) {
    		String patternForCurrentWord = getPatternGivenWord(word, currentSecretPattern, guess);
    		// If the map does not contain the pattern associated with the current word, 
    		// the pattern and the current word are added to the map
    		if (!(patternsWithAssociatedWords.containsKey(patternForCurrentWord))) {
    			addPatternAndWordToMap(word, patternForCurrentWord, patternsWithAssociatedWords);
    		}
    		// If the map does contain the pattern associated with the current word, 
    		// the existing entry containing the pattern for the current word is updated with 
    		// the current word
    		else {
    			patternsWithAssociatedWords.get(patternForCurrentWord).add(word);
    		}
    	}
    	
    	TreeMap<String, Integer> patternsWithAssociatedNumWords = new TreeMap<String, Integer>();
    	
    	// Adds each pattern and the associated number of words for each pattern to a new map
    	for (String p : patternsWithAssociatedWords.keySet()) {
    		patternsWithAssociatedNumWords.put(p, patternsWithAssociatedWords.get(p).size());	
    	}
    	
    	// Creates a new sorted ArrayList containing all of the patterns and their associated 
    	// number of words
    	ArrayList<WordFamily> sortedMapOfPatternsAndFrequencies = 
    			sortMapOfPatternsAndNumberOfWords(patternsWithAssociatedNumWords);
    	// Updates the pattern based on the pattern chosen from the sorted map of patterns
    	currentSecretPattern = updateCurrentPattern(sortedMapOfPatternsAndFrequencies);
    	// Checks whether the most updated pattern contains the guessed letter
    	// If it does not, decrements the number of guesses that the user has left
        if (!(checkIfPatternContainsGuessedLetter(currentSecretPattern, guess))) {
        	totalNumberOfGuessesLeft--;
        }

    	// Updates the active word list based on the most recent pattern
    	updateActiveWordsList(activeWords, patternsWithAssociatedWords, currentSecretPattern);
    	
    	if (debuggingVar) {
    		System.out.println("DEBUGGING: New pattern is: " + currentSecretPattern 
    				+ ". New family has " + numWordsCurrent() + " words.");
    	}
    	
    	return patternsWithAssociatedNumWords;
    }
    
    /**
     * Adds the given pattern and word to a map containing patterns and their associated words
     * @param word
     * @param patternForCurrentWord
     * @param patternsWithAssociatedWords
     */
    private void addPatternAndWordToMap(String word, String patternForCurrentWord, 
    		TreeMap<String, ArrayList<String>> patternsWithAssociatedWords) {
		ArrayList<String> wordsForPattern = new ArrayList<String>();
		wordsForPattern.add(word);
		patternsWithAssociatedWords.put(patternForCurrentWord, wordsForPattern);
    }
    
    /**
     * Returns a String containing the pattern associated with a word given the character that
     * was guessed and the current pattern
     * @param word
     * @param currentPattern
     * @param guess
     * @return a String containing the pattern associated with a given word
     */
    private String getPatternGivenWord(String word, String currentPattern, char guess) {
    	StringBuilder s = new StringBuilder();
    	char[] charactersInWord = word.toCharArray();
    	// Checks whether each character in the given word is the same as or different from 
    	// the guessed character
    	for (int i = 0; i < charactersInWord.length; i++) {
    		// If the current character in the given word is the same as the guessed character, 
    		// adds the guessed character to the String containing the pattern associated with the
    		// given word
    		if (charactersInWord[i] == guess) {
    			s.append(guess);
    		}
    		// Otherwise adds the character from the current pattern, that is at the same index of 
    		// the current character, to the String containing the pattern representing the 
    		// given word
    		else {
    			s.append(currentPattern.charAt(i));
    		}
    	}
    	return s.toString();
    }
    

    /**
     * Returns an ArrayList of patterns that is sorted by the number of associated words for  
     * each pattern, number of dashes for each pattern, and lexicographically by pattern
     * @param patternsWordFreqs
     * @return an ArrayList of patterns sorted by the number of words, number of dashes in each 
     * pattern, and lexicographically by pattern
     */
    private ArrayList<WordFamily> sortMapOfPatternsAndNumberOfWords(
    		TreeMap<String, Integer> patternsWordFreqs){
        ArrayList<WordFamily> sortedListOfPatterns = new ArrayList<WordFamily>();
        // Creates a new WordFamily object for each pattern, adds the new WordFamily object
        // to a list containing all of the patterns, and sorts the list of patterns
    	for (String p : patternsWordFreqs.keySet()) {
        	WordFamily newSWF = new WordFamily(p, patternsWordFreqs.get(p));
        	sortedListOfPatterns.add(newSWF);
            Collections.sort(sortedListOfPatterns);

        }
    	return sortedListOfPatterns;
    }
    
    
    /**
     * Returns a String representing the newest pattern based on the difficulty of the game,
     * the number of guesses made so far, and the current list of patterns 
     * @param sortedListOfPatterns
     * @return a String containing the newest pattern 
     */
    private String updateCurrentPattern(ArrayList<WordFamily> sortedListOfPatterns) {
    	String newPattern = "";
    	int index = 0;
    	final int NUMBER_TWO = 2;
    	final int NUMBER_FOUR = 4;
    	
    	// Given that the list of patterns contains more than one element, checks whether one of 
    	// two cases is true: one, the game is medium in difficulty and the number of the 
    	// current guess is a multiple of 4 OR two, the game is easy in difficulty and the number
    	// of the current guess is a multiple of 2 
    	
    	// If either of these two cases is true, picks the second hardest pattern to be the 
    	// new pattern
    	if (sortedListOfPatterns.size() > 1) {
    		if (((difficultyOfGame == HangmanDifficulty.MEDIUM) 
    				&& ((currentGuessNumber % NUMBER_FOUR) == 0)) 
    				|| ((difficultyOfGame == HangmanDifficulty.EASY) 
    						&& ((currentGuessNumber) % NUMBER_TWO) == 0)) {
    			index++;
    			if (debuggingVar) {
    				System.out.println("\nDEBUGGING: Picking second hardest list.");
    			}
    	    }
    		if (debuggingVar && index == 0) {
    	    		System.out.println("\nDEBUGGING: Picking hardest list.");
    	    }
    	}
    	newPattern = sortedListOfPatterns.get(index).pattern;
    	return newPattern;
    }
    
    
    /**
     * Returns a boolean value indicating whether a given pattern contains a certain character
     * @param secretPattern
     * @param guess
     * @return true if the given pattern contains the specified character and false otherwise
     */
    private boolean checkIfPatternContainsGuessedLetter(String secretPattern, char guess) {
    	// Checks whether any character in the secret pattern is the guessed character
    	for (int i = 0; i < secretPattern.length(); i++) {
    		if (secretPattern.charAt(i) == guess) {
    			return true;
    		}
    	}
    	return false;
    }

    
    /**
     * Updates the active list of words based on the current pattern by adding only words that have
     * the same pattern as the current pattern to the active list of words
     * @param listOfActiveWords
     * @param patternsAndWords
     * @param mostRecentPattern
     */
    private void updateActiveWordsList(ArrayList<String> listOfActiveWords, 
    		TreeMap<String, ArrayList<String>> patternsAndWords, String mostRecentPattern) {
    	listOfActiveWords.clear();
    	// Checks whether each pattern is the same as the current pattern and adds all of the words 
    	// with the same pattern as the current pattern to the list of active words
    	for (String p : patternsAndWords.keySet()) {
    		if (p.equals(mostRecentPattern)) {
    			for (int i = 0; i < patternsAndWords.get(p).size(); i++) {
    				listOfActiveWords.add(patternsAndWords.get(p).get(i));
    			}
    		}
    	}
    }
    
    
    /**
     * Returns the number of dashes in a given pattern
     * @param pattern
     * @return the number of dash characters in a given pattern
     */
    private int getNumDashes(String pattern) {
    	int numDashes = 0;
    	char[] charactersInPattern = pattern.toCharArray();
    	// Checks whether each character in a given pattern is a dash
    	for (char charInPattern : charactersInPattern) {
    		if (charInPattern == '-') {
    			numDashes++;
    		}
    	}
    	return numDashes;
    }


    /**
     * Return the secret word this HangmanManager finally ended up
     * picking for this round.
     * If there are multiple possible words left one is selected at random.
     * <br> pre: numWordsCurrent() > 0
     * @return return the secret word the manager picked.
     */
    public String getSecretWord() {
    	if (activeWords.size() == 0) {
    		throw new IllegalStateException("The size of the list of active words is 0.");
    	}
    	// If the list of active words contains only one word, return that word
    	if (activeWords.size() == 1) {
    		return activeWords.get(0);
    	}
    	// If the list of active words contains more than one word, return a random word from 
    	// the list
    	else {
    		Random randomWord = new Random();
    		int indexOfRandomWord = randomWord.nextInt(activeWords.size());
    		return activeWords.get(indexOfRandomWord);
    	}
    }
    
    
    /**
     * This class implements the Comparable interface to allow for patterns to be sorted by
     * the frequency of their associated words, the number of dashes the patterns contain, and
     * lexicographically
     *
     */
    public class WordFamily implements Comparable<WordFamily>{
    	String pattern;
    	int wordFrequency;
    	
    	public WordFamily(String p, int wf) {
    		pattern = p;
    		wordFrequency = wf;
    	}
    	
    	/**
    	 * Returns a value indicating whether this pattern is more difficult to guess than another
    	 * pattern by comparing two patterns by their number of associated words, the number of
    	 * dashes they contain, and lexicoraphically
    	 */
    	public int compareTo(WordFamily otherPattern) {
    		// Checks whether this pattern has the same number of words associated with it as 
    		// the other pattern
    		if (wordFrequency == otherPattern.wordFrequency) {
    			// If this pattern does have the same number of words as the other pattern, 
    			// compares the number of dashes in each pattern
    			if (getNumDashes(pattern) == getNumDashes(otherPattern.pattern)) {
    				// If this pattern has the same number of dashes as the other pattern, 
    				// compares both patterns lexicographically
    				return pattern.compareTo(otherPattern.pattern);

    			}
    			return  getNumDashes(otherPattern.pattern) - getNumDashes(pattern);
    		}
    		return otherPattern.wordFrequency - wordFrequency;

    	}
    }
}
