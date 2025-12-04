package carkifelek;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Game {
	

	Stack highscore_names = new Stack(10);
	Stack highscore_scores = new Stack(10);
	Stack alphabet = new Stack(26);
	Stack countryList = new Stack(15);
	String choosenCountry;
	int number;
	int playerScore;
	int step = 1;
	int indexCounter;     //this holds in which cell the choosen letter is.
	String choosenLetter;
	int addScore;// this holds how much point should system add to the player score
	String gain; // this is just for printing screen in wheel part.
	boolean gameOver = false;
	
	
	public Game() throws FileNotFoundException {
		
		fileOperations();
		createAlphabet();
		playGame();
		
	}
		
	
	public void fileOperations() throws FileNotFoundException {
	 
	File countries = new File("countries.txt");
	Scanner reader_2 = new Scanner(countries);
	
	while (reader_2.hasNextLine()) {
		String str = reader_2.nextLine().toLowerCase();
		countryList.push(str);						
	}
	reader_2.close();
	countryList = sortWordsStack(countryList);

	
	
	
	File highscore = new File("High_Score_Table.txt");
	Scanner read = new Scanner(highscore);
	while(read.hasNextLine()) {
		String line = read.nextLine();
		String[] values = line.split("#");
		String name = values[0];
		int score = Integer.parseInt(values[1]);		
		
		highscore_names.push(name);
		highscore_scores.push(score);		
	}
	read.close();
	}
	
	public void createAlphabet() {
		
		for (int i = 122; i > 96; i--) {	
			alphabet.push((char)i);
		}
	}
	
	
	public void playGame() {
		
		
		choosenCountry = chooseStack(countryList);
		System.out.println("Randomly generetad number is: " + number);
		
		Queue choosenWord = new Queue(choosenCountry.length());
		Queue guess = new Queue(choosenCountry.length());
		
		for (int i = 0; i < choosenCountry.length(); i++) { 	
			guess.enqueue("-");
			choosenWord.enqueue(choosenCountry.charAt(i));
			}
		
		while(true) {
			
		
		Random random = new Random();
		
		int wheel = 1+random.nextInt(8);
		
		if (wheel == 1) 	{
			gain = "10";	
			addScore = 10;
		}
		else if(wheel == 2) {
			gain = "50";
			addScore = 50;
		}
		else if(wheel == 3) {
			gain = "100";
			addScore = 100;	
		}
		else if(wheel == 4) {
			gain = "250";
			addScore = 250;
		}
		else if(wheel == 5) {
			gain = "500";
			addScore = 500;
		}		
		else if(wheel == 6) {
			gain = "1000";
			addScore = 1000;
		}
		else if(wheel == 7) {
			gain = "Double Money";
			addScore = playerScore;
		}
		else if(wheel == 8) {
			gain = "Bankrupt";
			playerScore = 0;
		}
		
		choosenLetter = chooseStack(alphabet);	
		
		display(guess);
		
		if (choosenCountry.contains(choosenLetter)) {
			playerScore = playerScore + addScore;
		}
	
		
		indexFinder(choosenWord, guess);
		
		isGameOver(guess);
		
		if (gameOver == true) 
		{
			System.out.println("\n" + "Word: " + choosenCountry + "               " + "Step: " + step + "      " + "Score: " + playerScore);
			
			System.out.println();
			
			System.out.println("YOU WIN " + playerScore + "$ !!!" + "\n");
			
			System.out.println("High Score Table");
			
			newHighScoreTable();
			
			
			break;
			
			
			
		}
		
		step++;
		
	
		
			}
		
		}
	
	public void newHighScoreTable() {
		
	int size = highscore_scores.size();
	Stack temp = new Stack(highscore_scores.size());
	Stack temp2 = new Stack(highscore_scores.size());
	
	for (int i = 0; i < size - 2; i++) {
		temp.push(highscore_scores.pop());
		temp2.push(highscore_names.pop());
	}
	if ((int) highscore_scores.peek() < playerScore) {
		highscore_scores.pop();
		highscore_names.pop();
		temp.push(playerScore);
		temp2.push("Player");
	}	
	else {
		temp.push(highscore_scores.pop());
		temp2.push(highscore_names.pop());
	}
	sortHighScoreStack(temp, temp2);
		
	}
	

	public void isGameOver(Queue guess) {
		
		
	 int counter = 0;
	 Queue temp = new Queue(guess.size());
	 
	 while(!guess.isEmpty()) {
		 String temp1 = guess.dequeue().toString();
		 temp.enqueue(temp1);
		 if (!temp1.equals("-")) { 
			 counter++;
			
		 }
		 if(counter == choosenCountry.length()) {
			 gameOver = true;
		 }
	 }
	 while(!temp.isEmpty()) {
		 guess.enqueue(temp.dequeue());
	 }
		
		
		
		
	}
	
	
	public void display(Queue guess) {
		
		System.out.print("Word:  ");
		
		displayQueue(guess);
		
		System.out.print("                " + "Step: " + step + "      " + "Score: " + playerScore + "            ");
		
		displayStack(alphabet);
		
		System.out.println();
		
		System.out.println("Wheel: " + gain);
		
		System.out.print("Guess: " + choosenLetter.toUpperCase());
		
		System.out.println("\n");
		}
	
	public Queue indexFinder(Queue countryWord, Queue guess) {
		
		indexCounter = 0;
		Queue temp = new Queue(countryWord.size());
		
		while(!countryWord.isEmpty()) {
			String temp1 = countryWord.dequeue().toString();	
			temp.enqueue(temp1);
			indexCounter++;                                    //holds which index the choosen letter 
			if (choosenLetter.equalsIgnoreCase(temp1)) {
				guess = letterChanger(guess);
			}
				
			
		}
		while(!temp.isEmpty()) {
			countryWord.enqueue(temp.dequeue());
		}
		return guess;
	}
	
	public Queue letterChanger(Queue q) {
		
		int counter = 1;
		Queue temp = new Queue(15);
		
		while(!q.isEmpty()) {
			temp.enqueue(q.dequeue());
		}
		while(!q.isFull()) {
			
			if (indexCounter == counter) {  //counter follows when should insert the letter in queue
				q.enqueue(choosenLetter);
				temp.dequeue();
			}
			else {
				q.enqueue(temp.dequeue());
			}
			counter++;
			
			
		}
		return q;
	}
	
	
	public String chooseStack(Stack s) {
			
			number = 0;
			Random rnd = new Random();
			number = 1+rnd.nextInt(s.size());
			Stack temp = new Stack(s.size());
			
			for(int i = 0; i < number - 1; i++) {
				temp.push(s.pop());				
			}
			String choosenObject = s.pop().toString().toLowerCase();
			int tempsize = temp.size();
			
			for (int j = 0; j < tempsize; j++) {
				s.push(temp.pop());
			}
			return choosenObject;
	} 
	
	
	public Stack sortWordsStack(Stack sortedStack) {
		
		
		int size = sortedStack.size();
		Stack tempStack = new Stack(size);
		while(!sortedStack.isEmpty()) {
			String temp = sortedStack.pop().toString();
			while(!tempStack.isEmpty() && temp.compareTo((tempStack.peek().toString())) > 0) {
				sortedStack.push(tempStack.pop());
			}
			tempStack.push(temp);
					
		}
		
		
		return tempStack;
		
	}
	public void sortHighScoreStack(Stack sortedNumbers, Stack sortedNames) {
		
		int size = sortedNumbers.size();
		Stack tempStack = new Stack(size);
		Stack tempStack2 = new Stack(size);
		while(!sortedNumbers.isEmpty()) {
			int temp = (int) sortedNumbers.pop();
			String temp1 = sortedNames.pop().toString();
			while(!tempStack.isEmpty() && (int) tempStack.peek() > temp) {
				sortedNumbers.push(tempStack.pop());
				sortedNames.push(tempStack2.pop());
			}
			tempStack.push(temp);
			tempStack2.push(temp1);
					
		}
		
		displayHighScoreTable(tempStack, tempStack2);
		
		
	}
	public void displayHighScoreTable(Stack scores, Stack names) {
		
		Stack temp1 = new Stack(scores.size());
		Stack temp2 = new Stack(scores.size());
		int size = scores.size();
		
		for(int i = 0; i < size; i++) {
			String temp = scores.pop().toString();
			String temp3 = names.pop().toString();
			temp1.push(temp);
			temp2.push(temp3);
			System.out.println(temp + "     " + temp3);
			
		}
		while(!temp1.isEmpty()){
			scores.push(temp1.pop());
			names.push(temp2.pop());
		}
		
		
	}
	public void displayStack(Stack s) {
		
		Stack temp1 = new Stack(s.size());
		int size = s.size();
		
		for(int i = 0; i < size; i++) {
			String temp = s.pop().toString();
			temp1.push(temp);
			System.out.print(temp);
			
		}
		while(!temp1.isEmpty()){
			s.push(temp1.pop());
			
		}
	}
	public void displayQueue(Queue q) {
		
		Queue temp1 = new Queue(q.size());
		while(!q.isEmpty()) {
			String temp = q.dequeue().toString();
			temp1.enqueue(temp);
			System.out.print(temp);	
}
		while(!temp1.isEmpty()) {
			q.enqueue(temp1.dequeue());
		}
		
			
		
	}
}


