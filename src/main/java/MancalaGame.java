import java.util.Scanner;


public class MancalaGame {

    public static void main(String[] args) {
        MancalaBoard mancalaGame;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Mancala");
        System.out.println("Enter Game Method:\n1. Human vs AI\t2. AI vs AI\t");
        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.println("Enter AI Heuristic: ");
            E_HEURISTICS heuristics = GetHeuristic(scanner);
            mancalaGame = new MancalaBoard(E_HEURISTICS.H_HUMAN, heuristics);
            mancalaGame.GameLoop();
        } else if (choice == 2) {
            System.out.println("1. Single matchup\t2. All possible matchups\t");
            int choice2 = scanner.nextInt();

            if (choice2 == 1) {
                System.out.println("Enter First AI Heuristic: ");
                E_HEURISTICS heuristics1 = GetHeuristic(scanner);
                System.out.println("Enter Second AI Heuristic: ");
                E_HEURISTICS heuristics2 = GetHeuristic(scanner);
                mancalaGame = new MancalaBoard(heuristics1, heuristics2);
                mancalaGame.GameLoop();
            } else if (choice2 == 2) {
                for (int i = 0; i < e_heuristics.length; i++) {//player 1
                    for (int j = 0; j < e_heuristics.length; j++) {//player 2

                        for (int k = 0; k < MATCH_PER_HEURISTIC; k++) {//, trials deterministic, only needs one game, same every time
                            System.out.println("--- matchup H" + (i+1) + " vs H" + (j+1) + " ---");
                            mancalaGame = new MancalaBoard(e_heuristics[i], e_heuristics[j]);
                            mancalaGame.GameLoop();

                            System.out.println();
                            System.out.println();

                        }
                        System.out.println();

                    }
                }
            } else {
                System.out.println("Not a valid choice");
            }


        } else {
            System.out.println("Not a valid choice");
        }
    }


    public static E_HEURISTICS GetHeuristic(Scanner scanner){
        PrintHeuristics();
        int Choice = scanner.nextInt();
        if(Choice-1 < 0 || Choice-1 >= e_heuristics.length){
            System.out.println("Invalid option, aborting");
            System.exit(-1);
        }
        return e_heuristics[Choice-1];
    }
    public static int BIN_COUNT = 6;
    public static int INITIAL_STONE_PER_BIN = 4;
    public static int MAX_DEPTH = 8;
    public static int INFINITY = Integer.MAX_VALUE;
    public static int NEG_INFINITY = Integer.MIN_VALUE;
    public static int MATCH_PER_HEURISTIC = 1;//deterministic, only needs one game, same every time

    static E_HEURISTICS[] e_heuristics = {E_HEURISTICS.H_STORAGE_DIFFERENCE, E_HEURISTICS.H_STONE_AND_STORAGE, E_HEURISTICS.H_CHAIN_MOVES_PRIORITIZE,
            E_HEURISTICS.H_STONES_CLOSE_TO_MY_STORAGE, E_HEURISTICS.H_CLOSE_TO_WINNING, E_HEURISTICS.H_REDUCE_OPPONENT_POINT, E_HEURISTICS.H_RANDOM};



    public static void PrintHeuristics(){
        int Counter = 0;
        for(E_HEURISTICS eHeuristics : e_heuristics){
            System.out.print(E_HEURISTICS.ToString(eHeuristics));
            Counter++;
            if(Counter%2==0){
                System.out.println();
            }else{
                System.out.print("\t");
            }
        }
    }


}

enum E_HEURISTICS{
    H_STORAGE_DIFFERENCE,
    H_STONE_AND_STORAGE,
    H_CHAIN_MOVES_PRIORITIZE,
    H_STONES_CLOSE_TO_MY_STORAGE,
    H_CLOSE_TO_WINNING,
    H_REDUCE_OPPONENT_POINT,
    H_RANDOM,
    H_HUMAN;

    public static String ToString(E_HEURISTICS e_heuristics){
        switch (e_heuristics){
            case H_STORAGE_DIFFERENCE -> {
                return "(1) Storage Difference";
            }
            case H_HUMAN -> {
                return "Human";
            }
            case H_RANDOM -> {
                return "(7) Random";
            }
            case H_STONE_AND_STORAGE -> {
                return "(2) Storage Difference + Stone Difference";
            }
            case H_CHAIN_MOVES_PRIORITIZE -> {
                return "(3) Storage Difference + Stone Difference + Additional Move";
            }
            case H_STONES_CLOSE_TO_MY_STORAGE -> {
                return "(4) Stones Closer to Storage";
            }
            case H_REDUCE_OPPONENT_POINT -> {
                return "(6) Reduce Opponent Point";
            }
            case H_CLOSE_TO_WINNING -> {
                return "(5) Close To Winning";
            }
            default -> {
                return "Error";
            }
        }
    }
}