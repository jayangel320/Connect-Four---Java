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
        String[][] copy = new String[6][7];
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                copy[r][c] = blank[r][c];
            }
        }
        return copy;
    }


    //--------------------------------------------------------------------------//
    //                  Recursive AI
    //--------------------------------------------------------------------------//

    //This AI is very expensive in terms of computations, even looking 4 turns ahead crashes the program
    //can do 3 recursions with no serious delay
//TODO: gets stuck when users column choice is 7

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
        int[] copy = new int[42];
        for (int r = 0; r < 42; r++) {
            copy[r] = moves[r];
        }
        return copy;
    }

    //--------------------------------------------------------------------------//
    //      This takes the users input and safes it as a String
    //--------------------------------------------------------------------------//

    public static String question() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    //--------------------------------------------------------------------------//
    //                  refresh board to empty state
    //--------------------------------------------------------------------------//

    public static String[][] refresh(String[][] screen) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                screen[i][j] = ".";
            }
        }
        return screen;
    }

    //--------------------------------------------------------------------------//
    //                  displays connect four board
    //--------------------------------------------------------------------------//

    public static void display(String[][] screen) {
        System.out.println("");
        for (int i = 0; i < 6; i++) {
            System.out.printf("|");
            for (int j = 0; j < 7; j++) {
                System.out.printf(" %s ", screen[i][j]);
            }
            System.out.println("|");
        }
        System.out.printf("|");
        for (int i = 0; i < 7; i++) {
            System.out.printf(" - ");
        }
        System.out.println("|");

        System.out.printf("|");
        for (int i = 1; i < 8; i++) {
            System.out.printf(" %s ", i);
        }
        System.out.println("|");
    }

    //--------------------------------------------------------------------------//
    //                          Asks users initial questions
    //--------------------------------------------------------------------------//

    public static boolean promptUser() throws IOException {
        String[] bubbles = {"What is your name?: ", "would you like to play connect four?: "};
        System.out.printf("%s", bubbles[0]);
        String answer = question();
        System.out.printf("%n%s %s", answer, bubbles[1]);
        answer = question().toLowerCase();
        if (answer.contains("yes")) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------//
    //                                     Run!!!!! with betterAIchoice
    //--------------------------------------------------------------------------//


    public static void run() throws IOException {
        String[][] blank = new String[6][7];
        blank = refresh(blank);
        display(blank);
        while (true) {
            int columnChoice = Integer.parseInt(question()) - 1;
            int[] array = checkChoice(columnChoice, blank);
            if (array[2] == 1) {
                blank[array[1]][columnChoice] = "x";
                display(blank);
                if (checkIfWin(blank, "x")) {
                    System.out.println("YOU WON!!!");
                    break;
                }
                array = aiChoice(columnChoice,blank);
                blank[array[1]][array[0]] = "o";
                //blank = aiChoice(columnChoice, blank);
                System.out.printf("%nThe AI's Choice!%n");
                display(blank);
                if (checkIfWin(blank, "o")) {
                    System.out.println("YOU LOST!!!");
                    break;
                }
            } else {
                System.out.printf("%nPlease choose again");
                continue;
            }
        }
    }

    //--------------------------------------------------------------------------//
    //                      Check if someone won!
    //--------------------------------------------------------------------------//

    private static boolean checkIfWin(String[][] blank, String mark) {
        for (int i = 5; i > -0; i--) {
            for (int j = 0; j < 7; j++) {

                if (blank[i][j] == mark) {
                    int k = 1;
                    int x = 1;
                    int y = 1;
                    if (j < 4) {
                        if (i > 2) {
                            while (k < 4 && blank[i - k][j] == mark) {
                                //System.out.printf("%n%s%n",blank[i-k][j] );
                                k++;
                            }
                            while (y < 4 && blank[i - y][j + y] == mark) {
                                //System.out.printf("%n%s%n",blank[i-y][j+y] );
                                y++;
                            }
                        }
                        while (x < 4 && blank[i][j + x] == mark) {
                            //System.out.printf("%n%s  ( %s, %s) %n",blank[i][j+x],i,j+x );
                            x++;
                        }
                    } else if (j > 3) {
                        if (i > 2) {
                            while (x < 4 && blank[i - x][j] == mark) {
                                //System.out.printf("%n%s%n",blank[i-x][j] );
                                x++;
                            }
                            while (y < 4 && blank[i - y][j - y] == mark) {
                                // System.out.printf("%n%s%n",blank[i-y][j-y] );
                                y++;
                            }
                        }
                    }
                    if (k == 4 || y == 4 || x == 4) {
                        //System.out.printf("%n %s %s %s %s %s %n", k, y, x, i, j);
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
        if (columnInt < 7 && columnInt >= 0) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------//
    //                      check row
    //--------------------------------------------------------------------------//

    private static boolean checkR(int rowInt) {
        if (rowInt < 6 && rowInt >= 0) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------//
    //                  tells which row to place marker
    //--------------------------------------------------------------------------//

    public static int whichRow(String[][] screen, int columnInt) {

        int rowInt = 5;
        while (screen[rowInt][columnInt] == "x" || screen[rowInt][columnInt] == "o") {
            rowInt--;
            if (rowInt < 0) {
                break;
            }
        }
        return rowInt;
    }

    //--------------------------------------------------------------------------//
    //          checks user's/AI's choice
    //--------------------------------------------------------------------------//

    public static int[] checkChoice(int column, String[][] blank) {
        int[] choices = new int[3];
        choices[0] = column;
        choices[2] = 0;
        if (checkC(choices[0])) {
            int temp = whichRow(blank, choices[0]);
            if (checkR(temp)) {
                choices[1] = temp;
                choices[2] = 1;
            }
        }
        return choices;
    }

    //--------------------------------------------------------------------------//
    //          The AI
    //--------------------------------------------------------------------------//

    public static int[] aiChoice(int columnChoice, String[][] blank) {


        while (true) {
            int[] array;
            Random ran = new Random();
            int rowChoiceAI;
            if (columnChoice == 1) {
                rowChoiceAI = columnChoice + ran.nextInt(3);
            } else if (columnChoice == 7) {
                rowChoiceAI = columnChoice - ran.nextInt(3);
            } else {
                rowChoiceAI = columnChoice + (ran.nextInt(3) - 1);
            }
            array = checkChoice(rowChoiceAI, blank);
            if(array[2] == 1) {
                return array;
            }
        }

    }

    //--------------------------------------------------------------------------//
    //          a better Ai choice
    //--------------------------------------------------------------------------//

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
        //doesn't implement an move so fix that if you think this
        return blank;
    }


    //--------------------------------------------------------------------------//
    //       uses bestchoiceAi and then finds the best column to put AI in
    //--------------------------------------------------------------------------//


    public static int bestChoiceCol(int[] locDir, int sizeOfVect) {
        sizeOfVect--;
        switch (locDir[2]) {
            case 2:
                return locDir[1] - sizeOfVect;
            case 3:
                return locDir[1];
            case 4:
                return locDir[1] + sizeOfVect;
            case 5:
                return locDir[1] + sizeOfVect;
        }
        return 4;
    }


//--------------------------------------------------------------------------//
    //                                     Run!!!!! with Recursion AI
    //--------------------------------------------------------------------------//


    public static void runRecursion() throws IOException {
        String[][] blank = new String[6][7];
        blank = refresh(blank);
        display(blank);
        int moves[] = new int[43];
        moves[0] = 0;
        List<int[]> wins = new ArrayList<>();

        while (true) {
            int columnChoice = Integer.parseInt(question()) - 1;
            int[] array = checkChoice(columnChoice, blank);
            if (array[2] == 1) {
                blank[array[1]][columnChoice] = "x";
                moves[++moves[0]] = columnChoice;
                display(blank);
                if (checkIfWin(blank, "x")) {
                    System.out.println("YOU WON!!!");
                    break;
                }
                if(moves[0] < 4) {
                    wins = RecursiveAi(blank, 2, moves, wins);
                    array = checkWins(wins,blank,moves,columnChoice);
                }
                else
                {
                    array = aiChoice(columnChoice,blank);
                }
                blank[array[1]][array[0]] = "o";
                moves[++moves[0]] = array[0];
                System.out.printf("%nThe AI's Choice!%n");
                display(blank);
                if (checkIfWin(blank, "o")) {
                    System.out.println("YOU LOST!!!");
                    break;
                }
            } else {
                System.out.printf("%nPlease choose again");
                continue;
            }
        }
    }


    //--------------------------------------------------------------------------//
    //     Chooses RecursionAI column choice by randomly selecting next step
    //      from a list of winning moves then returns column and row for AI
    //--------------------------------------------------------------------------//

    public static int[] checkWins(List<int[]> wins, String[][] blank, int[] moves,int column) {
        if(moves[0]< 4 || wins.size() == 0)
        {
         int[] choice = aiChoice(column,blank);
            return choice;
        }

        int counter = 0;
        List<Integer> matchingMoves = new ArrayList<>();
        for (int[] tempMoves : wins) {
            if (tempMoves[moves[0]] == moves[moves[0]]) {
                matchingMoves.add(counter);
            }
            counter++;
        }
        Random ran = new Random();
        column = wins.get(ran.nextInt(matchingMoves.size()))[4];
        int[] choices = checkChoice(column, blank);
        return choices;
    }

}


