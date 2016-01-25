package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Main {

    public static void main(String[] args) throws IOException {


        if (promptUser()) {
            run();
        }




    }

    //--------------------------------------------------------------------------//
    //                  deep copy of board
    //--------------------------------------------------------------------------//

    public static String[][] deepCopy(String[][] blank) {
        //creates new 2dim string array
        String[][] copy = new String[6][7];
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                //cycles through game board and copies values to new array
                copy[r][c] = blank[r][c];
            }
        }
        //returns copy
        return copy;
    }


    //--------------------------------------------------------------------------//
    //                  Recursive AI
    //--------------------------------------------------------------------------//

    //This AI is very expensive in terms of computations, even looking 4 turns ahead crashes the program
    //can do 3 recursions with no serious delay
//TODO: gets stuck when users column choice is 7

    //checks every possible combinations of moves from implementation of this method on and adds those that win
    // to a list of int arrays
    public static List<int[]> RecursiveAi(String[][] blank, int recursions, int[] moves, List<int[]> wins) {
        //cycles through all possible moves for ai and user
        for (int AIchoice = 0; AIchoice < 7; AIchoice++) {
            //makes a temp copy of recursions
            int tempRecursions = recursions;

            // makes a deep copy of board
            String[][] tempBlank = deepCopy(blank);

            //makes deep copy of moves
            int[] tempMoves = deepCopy(moves);

            // checks to make sure current column isn't full
            while (checkChoice(AIchoice, tempBlank)[2] != 1) {
                AIchoice = (++AIchoice) % 7;
            }

            //finds row from column choice and existing board
            int[] array = checkChoice(AIchoice, tempBlank);

            //places ai's marker on board
            tempBlank[array[1]][AIchoice] = "x";

            //add column choice to tempMoves array
            tempMoves[tempMoves[0]++] = AIchoice;

            //checks if ai won and skips this column choice
            if (checkIfWin(tempBlank, "x")) {
                continue;
            }

            //cycles through all possible user choices
            for (int UserChoice = 0; UserChoice < 7; UserChoice++) {

                //makes deep copy of board after ai's choice
                String[][] tempBlank2 = deepCopy(tempBlank);

                //makes a copy of recursions
                int tempRecursions2 = tempRecursions;

                //makes deep copy of tempMoves
                int[] tempMoves2 = deepCopy(tempMoves);

                //makes sure current column isn't full
                while (checkChoice(UserChoice, tempBlank2)[2] != 1) {
                    UserChoice = (++UserChoice) % 7;
                }

                //finds row from column
                array = checkChoice(UserChoice, tempBlank2);

                //puts users marker on board
                tempBlank2[array[1]][UserChoice] = "o";

                //add column choice to tempMoves array
                tempMoves2[tempMoves2[0]++] = UserChoice;

                //checks if user won
                if (checkIfWin(tempBlank2, "o")) {
                    //skips current column choice because user won
                    wins.add(moves);
                    //display(tempBlank2);
                    continue;
                }
                //this is the recursion part
                if (--tempRecursions2 > 0) {
                    wins = RecursiveAi(tempBlank2, tempRecursions2, tempMoves2, wins);
                }

            }
        }

        //returns a list of all the possible winning moves for three turns ahead
        return wins;
    }

    //--------------------------------------------------------------------------//
    //                  deep copy of moves
    //--------------------------------------------------------------------------//

    public static int[] deepCopy(int[] moves) {
        //creates new int array
        int[] copy = new int[42];
        for (int r = 0; r < 42; r++) {
            //cycles through moves and copies values to array
            copy[r] = moves[r];
        }
        //returns new array
        return copy;
    }

    //--------------------------------------------------------------------------//
    //      This takes the users input and safes it as a String
    //--------------------------------------------------------------------------//

    //Throws exception in case user input to string fails
    public static String question() throws IOException {
        //Creates a new buffered reader object
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //returns readLine which returns what the user inputs as a string
        return br.readLine();
    }

    //--------------------------------------------------------------------------//
    //                  refresh board to empty state
    //--------------------------------------------------------------------------//

    public static String[][] refresh(String[][] screen) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                //cycles through all of the values on game board and sets them to "."
                screen[i][j] = ".";
            }
        }
        // returns changed board
        return screen;
    }

    //--------------------------------------------------------------------------//
    //                  displays connect four board
    //--------------------------------------------------------------------------//

    public static void display(String[][] screen) {
        //indents board
        System.out.println("");
        //cycles through the rows of the board
        for (int i = 0; i < 6; i++) {
            //prints the left boarder of board
            System.out.printf("|");

            //cycles through the columns and prints the values
            for (int j = 0; j < 7; j++) {
                System.out.printf(" %s ", screen[i][j]);
            }
            //prints the right side boarder
            System.out.println("|");
        }
        //prints the bottom boarder of game board
        System.out.printf("|");
        for (int i = 0; i < 7; i++) {
            System.out.printf(" - ");
        }
        System.out.println("|");

        //prints out the labels for each column
        System.out.printf("|");
        for (int i = 1; i < 8; i++) {
            System.out.printf(" %s ", i);
        }
        System.out.println("|");
    }

    //--------------------------------------------------------------------------//
    //                          Asks users initial questions
    //--------------------------------------------------------------------------//

    //returns true if user wants to play the game
    public static boolean promptUser() throws IOException {
        //string array of things program will ask user
        String[] bubbles = {"What is your name?: ", "would you like to play connect four?: "};
        //asks user what there name is using string array values from above
        System.out.printf("%s", bubbles[0]);
        //calls method question which returns users response as a string
        String answer = question();
        System.out.printf("%n%s %s", answer, bubbles[1]);
        //calls method question which returns users response as a string
        answer = question().toLowerCase();
        //checks to see if user input yes and only yes
        if (answer.contains("yes")) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------//
    //                                     Run!!!!! with betterAIchoice
    //--------------------------------------------------------------------------//

//TODO add a break for when the board is full or maybe for when a tie is eminent
    public static void run() throws IOException {
        //Creates 2dim array that will be board
        String[][] blank = new String[6][7];
        //calls refresh Method of new board
        blank = refresh(blank);
        //displays empty board
        display(blank);
        //loops until someone wins see to do
        while (true) {
            //saves users column choice input as an int
            int columnChoice = Integer.parseInt(question()) - 1;
            //calls method checkChoice
            int[] array = checkChoice(columnChoice, blank);
            //if choice was valid adds marker to game board then calls AI
            if (array[2] == 1) {
                //adds marker to game board
                blank[array[1]][columnChoice] = "x";
                //displays gameboard
                display(blank);
                //checks to see if user won
                if (checkIfWin(blank, "x")) {
                    //tells user they won
                    System.out.println("YOU WON!!!");
                    //ends loop and ends program
                    break;
                }
                //calls aiChoice to determine AI's next move
                array = aiChoice(columnChoice,blank);
                //sets AI's marker to game board
                blank[array[1]][array[0]] = "o";
                //Tells user that AI has made a choice
                System.out.printf("%nThe AI's Choice!%n");
                //displays game board with users choice
                display(blank);
                //checks to see if AI won
                if (checkIfWin(blank, "o")) {
                    //tells user they lost
                    System.out.println("YOU LOST!!!");
                    //exits loop and ends program
                    break;
                }
            } else {
                //tells user to choose another value in case their input was not valid
                System.out.printf("%nPlease choose again");
                //goes to next iteration of while loop
                continue;
            }
        }
    }

    //--------------------------------------------------------------------------//
    //                      Check if someone won!
    //--------------------------------------------------------------------------//

    private static boolean checkIfWin(String[][] blank, String mark) {
        //cycles through the rows and columns
        for (int i = 5; i > -0; i--) {
            for (int j = 0; j < 7; j++) {
                //checks to see if each value matches mark which would either be "o" or "x"
                if (blank[i][j] == mark) {
                    //int counters for different methods of winning if any of these counters are incremented to 4
                    // that means that someone won
                    int k = 1;
                    int x = 1;
                    int y = 1;
                    //only checks diagonally to the right
                    if (j < 4) {
                        //skips checking verticaly and diagonally  if you are too high up on the board
                        if (i > 2) {
                            //checks for consecutive "marks" horizontally
                            while (k < 4 && blank[i - k][j] == mark) {
                                //System.out.printf("%n%s%n",blank[i-k][j] );
                                k++;
                            }
                            //checks for consecutive "marks" diagonally with the top to the right
                            while (y < 4 && blank[i - y][j + y] == mark) {
                                //System.out.printf("%n%s%n",blank[i-y][j+y] );
                                y++;
                            }
                        }
                        //checks for consecutive "marks" vertically
                        while (x < 4 && blank[i][j + x] == mark) {
                            //System.out.printf("%n%s  ( %s, %s) %n",blank[i][j+x],i,j+x );
                            x++;
                        }
                        //checks diagonally with the top to the left and horizontally
                    } else if (j > 3) {
                        if (i > 2) {
                            //checks vertically
                            while (x < 4 && blank[i - x][j] == mark) {
                                //System.out.printf("%n%s%n",blank[i-x][j] );
                                x++;
                            }
                            //checks diagonally with the top to the left
                            while (y < 4 && blank[i - y][j - y] == mark) {
                                // System.out.printf("%n%s%n",blank[i-y][j-y] );
                                y++;
                            }
                        }
                    }
                    //if any of the counters were incremented to 4 than method return true if not then returns false
                    if (k == 4 || y == 4 || x == 4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //--------------------------------------------------------------------------//
    //                      check column
    //--------------------------------------------------------------------------//

    private static boolean checkC(int columnInt) {
        //makes sure columnInt is valid integer value
        if (columnInt < 7 && columnInt >= 0) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------//
    //                      check row
    //--------------------------------------------------------------------------//

    private static boolean checkR(int rowInt) {
        //makes sure rowInt is a valid integer value
        if (rowInt < 6 && rowInt >= 0) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------//
    //                  tells which row to place marker
    //--------------------------------------------------------------------------//

    public static int whichRow(String[][] screen, int columnInt) {
        //rowInt counter starting from the bottom
        int rowInt = 5;
        //cycles up the column until the first blank spot is found
        while (screen[rowInt][columnInt] == "x" || screen[rowInt][columnInt] == "o") {
            rowInt--;
            if (rowInt < 0) {
                break;
            }
        }
        //the first blank row in the given column is returned as the row choice
        return rowInt;
    }

    //--------------------------------------------------------------------------//
    //          checks user's/AI's choice
    //--------------------------------------------------------------------------//

    public static int[] checkChoice(int column, String[][] blank) {
        //new array with three elements is created
        int[] choices = new int[3];
        //element 0 is the column choice
        choices[0] = column;
        //element 2 is the validity of the choices with its default value set to not valid ie 0
        choices[2] = 0;
        //implements checkC method on element 0
        if (checkC(choices[0])) {
            //if valid column choice the row choice is found using whichRow method
            int temp = whichRow(blank, choices[0]);
            //if valid row choice then added as element 1 in array and validity is set to true or 1
            if (checkR(temp)) {
                choices[1] = temp;
                choices[2] = 1;
            }
        }
        //returns array
        return choices;
    }

    //--------------------------------------------------------------------------//
    //          The AI
    //--------------------------------------------------------------------------//

    //very simple AI that chooses the next column based solely on where the user placed his marker
    public static int[] aiChoice(int columnChoice, String[][] blank) {

        //loops until valid column choice is found
        while (true) {
            //creates new array
            int[] array;
            //creates a new random object
            Random ran = new Random();
            //column Choice for ai variable
            int colChoiceAI;
            //randomly chooses either the same column or one the surrounding columns
            if (columnChoice == 1) {
                colChoiceAI = columnChoice + ran.nextInt(3);
            } else if (columnChoice == 7) {
                colChoiceAI = columnChoice - ran.nextInt(3);
            } else {
                colChoiceAI = columnChoice + (ran.nextInt(3) - 1);
            }
            //makes sure choice is valid and determine row choice
            array = checkChoice(colChoiceAI, blank);
            //if choice is valid then array containing validity, row and column choice is returned
            if(array[2] == 1) {
                return array;
            }
        }

    }

    //--------------------------------------------------------------------------//
    //          a better Ai choice
    //--------------------------------------------------------------------------//

    //The problem with this is it looks for consecutive "x"'s meaning any area where there are two or three in a row
    // and this is only defensive programing what it needs
    // TODO is search for consecutive "o"s as well and become offensive when necessary
    //TODO only searches for consecutive "marks" will miss if there are 2 in row then blank then 1 or any such combo

    //Reformatted the checkIfWin to look at where a win would be possible then add that column to a map
    public static String[][] betterAIChoice(String[][] blank) {
        String mark = "x";
        // This might work but not for when you have a diagonal and the winning move is the lowest spot
        // and not when the winning move is horizontal to the left
        Map<Integer, int[]> bestChoice = new TreeMap<>();
        int w = 1;
        int[] locationDirection = {0, 0, 0};
        for (int i = 5; i > -0; i--) {
            for (int j = 0; j < 7; j++) {

                if (blank[i][j] == mark) {
                    int k = 1;
                    int x = 1;
                    int y = 1;
                    if (j < 4) {
                        if (i > 2) {
                            while (blank[i - k][j] == mark && k < 4) {
                                //System.out.printf("%n%s%n",blank[i-k][j] );
                                if (blank[i - k++ - 1][j] == ".") {
                                    locationDirection[0] = i;
                                    locationDirection[1] = j;
                                    locationDirection[2] = 3;
                                    bestChoice.putIfAbsent(k, locationDirection);
                                }
                            }
                            while (blank[i - y][j + y] == mark && y < 4) {
                                //System.out.printf("%n%s%n",blank[i-y][j+y] );
                                if (blank[i - y - 1][j + y++ + 1] == ".") {
                                    locationDirection[0] = i;
                                    locationDirection[1] = j;
                                    locationDirection[2] = 4;
                                    bestChoice.putIfAbsent(y, locationDirection);
                                }
                            }
                        }
                        while (blank[i][j + x] == mark && x < 4) {
                            //System.out.printf("%n%s  ( %s, %s) %n",blank[i][j+x],i,j+x );
                            if (blank[i - x++ - 1][j] == ".") {
                                locationDirection[0] = i;
                                locationDirection[1] = j;
                                locationDirection[2] = 5;
                                bestChoice.putIfAbsent(x, locationDirection);
                            }
                        }
                    } else if (j > 3) {
                        if (i > 2) {
                            while (blank[i - x][j] == mark && x < 4) {
                                if (blank[i - x++ - 1][j] == ".") {
                                    locationDirection[0] = i;
                                    locationDirection[1] = j;
                                    locationDirection[2] = 3;
                                    bestChoice.putIfAbsent(x, locationDirection);
                                }
                            }
                            while (blank[i - y][j - y] == mark && y < 4) {

                                if (blank[i - y - 1][j - y++ - 1] == ".") {
                                    locationDirection[0] = i;
                                    locationDirection[1] = j;
                                    locationDirection[2] = 2;
                                    bestChoice.putIfAbsent(y, locationDirection);

                                }
                            }
                        }
                    }

                }
            }
        }
        if (bestChoice.size() > 0) {
            //saves size of map adding one to account for key starting at 2
            int sizeOfVect = bestChoice.size() + 1;
            //storing best choice of location based on above code
            int[] locDir = bestChoice.get(sizeOfVect);
            //deciding what the best column is
            int column = bestChoiceCol(locDir, sizeOfVect) + 1;
            //deciding the best row
            int[] loc = checkChoice(column, blank);
            //assigning the marker
            blank[loc[1]][column] = "o";
            //returning marker
            return blank;
        }
        //TODO: should be more random but ok for now
        int[] array =  aiChoice(4, blank);
        //doesn't implement a move so fix that if you think this will be usefull
        return blank;
    }


    //--------------------------------------------------------------------------//
    //       uses bestchoiceAi and then finds the best column to put AI in
    //--------------------------------------------------------------------------//


    public static int bestChoiceCol(int[] locDir, int sizeOfVect) {
        //determines which column the marker should in go based on the results of the betterAIChoice
        sizeOfVect--;
        //checks the 2 element in loc dir which determines the direction
        switch (locDir[2]) {
            //this is diagonally with top to the left
            case 2:
                return locDir[1] - sizeOfVect;
            //this is vertically
            case 3:
                return locDir[1];
            //this is diagonally with top to the right
            case 4:
                return locDir[1] + sizeOfVect;
            //this is horizontally to the right
            case 5:
                return locDir[1] + sizeOfVect;
        }
        //returns vertically if all else fails
        return 4;
    }


//--------------------------------------------------------------------------//
    //                                     Run!!!!! with Recursion AI
    //--------------------------------------------------------------------------//


    public static void runRecursion() throws IOException {
        //creates new game board
        String[][] blank = new String[6][7];
        //fills in blank gameboard
        blank = refresh(blank);
        //displays blank game board
        display(blank);
        //creates an int array that will keep track of all of the moves made
        int moves[] = new int[43];
        //the first element in moves is how many moves have been made
        moves[0] = 0;
        //creates a list of integer arrays that contain winning moves
        List<int[]> wins = new ArrayList<>();
        //loops until someone wins
        while (true) {
            //turns user input into an int
            int columnChoice = Integer.parseInt(question()) - 1;
            //finds validity and row choice
            int[] array = checkChoice(columnChoice, blank);
            //checks validity
            if (array[2] == 1) {
                //places users marker on game board
                blank[array[1]][columnChoice] = "x";
                //adds column choice to moves array
                moves[++moves[0]] = columnChoice;
                //displays game board
                display(blank);
                //checks to see if user won
                if (checkIfWin(blank, "x")) {
                    //tells user it won
                    System.out.println("YOU WON!!!");
                    //exits while loop and ends program
                    break;
                }
                //calls a simple AI if early on in game and calls Recursive Ai after two turns
                if(moves[0] < 4) {
                    //calls recursiveAi method
                    wins = RecursiveAi(blank, 2, moves, wins);
                    //returns column and choice and validity into int array
                    array = checkWins(wins,blank,moves,columnChoice);
                }
                else
                {
                    //calls simple AI
                    array = aiChoice(columnChoice,blank);
                }
                //adds Ai markers to gameboard
                blank[array[1]][array[0]] = "o";
                //adds column choice to moves array
                moves[++moves[0]] = array[0];
                //tells user Ai has made its move
                System.out.printf("%nThe AI's Choice!%n");
                //displays updated game board
                display(blank);
                //checks if AI won
                if (checkIfWin(blank, "o")) {
                    //tell user they lost
                    System.out.println("YOU LOST!!!");
                    //exits loop and ends program
                    break;
                }
            } else {
                //tells user to enter a new valid input
                System.out.printf("%nPlease choose again");
                //continues to next iteration of while loop
                continue;
            }
        }
    }


    //--------------------------------------------------------------------------//
    //     Chooses RecursionAI column choice by randomly selecting next step
    //      from a list of winning moves then returns column and row for AI
    //--------------------------------------------------------------------------//


    //returns int array that contains column and row choice and whether those values are valid
    public static int[] checkWins(List<int[]> wins, String[][] blank, int[] moves,int column) {
        //skips code if the game is so early that the recursive AI is useless or the recursive AI finds no winning moves
        if(moves[0]< 4 || wins.size() == 0)
        {
            //calls simple AI method defined above
         int[] choice = aiChoice(column,blank);
            //array from simple AI method
            return choice;
        }
        //counter that will check how many of the winning moves match the current games moves
        int counter = 0;
        //creates a new integer list that will contain all matching winning moves index number
        List<Integer> matchingMoves = new ArrayList<>();
        for (int[] tempMoves : wins) {
            //checks to see if the last move matches any of the same indexed moves from the winning moves list
            if (tempMoves[moves[0]] == moves[moves[0]]) {
                //adds matching moves index to integer list
                matchingMoves.add(counter);
            }
            //increments counter
            counter++;
        }
        //randomly chooses one of the matching winning moves list and chooses to implement the next move on that array
        Random ran = new Random();
        column = wins.get(ran.nextInt(matchingMoves.size()))[moves[0] + 1];
        //makes sure column choice is valid and finds row choice
        int[] choices = checkChoice(column, blank);
        //returns row and column choice
        return choices;
    }
}


