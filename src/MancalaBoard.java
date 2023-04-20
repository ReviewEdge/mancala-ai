import java.util.Scanner;

public class MancalaBoard {
    private final int BinNumber = MancalaGame.BIN_COUNT;

    private boolean bSteal = false;
    private boolean bMoveAgain = false;
    private int StonesCaptured = 0;

    private boolean bNoPrint = false;

    private final Player[] Players = new Player[2];
    private Player CurrentMove = null;
    private Player Opponent = null;

    private GameStatus gameStatus;

    public MancalaBoard(){gameStatus = null;}

    public MancalaBoard(E_HEURISTICS playerOneHeuristic, E_HEURISTICS playerTwoHeuristic) {
        String PlayerOne, PlayerTwo;

        if(playerOneHeuristic == E_HEURISTICS.H_HUMAN){
            if(playerTwoHeuristic == E_HEURISTICS.H_HUMAN){
                PlayerOne = "Human-1";
                PlayerTwo = "Human-2";
            }else{
                PlayerOne = "Human";
                PlayerTwo = "AI";
            }
        }else{
            if(playerTwoHeuristic == E_HEURISTICS.H_HUMAN){
                PlayerOne = "AI";
                PlayerTwo = "Human";
            }else{
                PlayerOne = "AI-1 (" + E_HEURISTICS.ToString(playerOneHeuristic) + ")";
                PlayerTwo = "AI-2 (" + E_HEURISTICS.ToString(playerTwoHeuristic) + ")";
            }
        }

        Players[0] = new Player(playerOneHeuristic, BinNumber, MancalaGame.INITIAL_STONE_PER_BIN, PlayerOne);
        Players[1] = new Player(playerTwoHeuristic, BinNumber, MancalaGame.INITIAL_STONE_PER_BIN, PlayerTwo);
        Players[0].SetOpponent(Players[1]);
        Players[1].SetOpponent(Players[0]);
        CurrentMove = Players[0];
        Opponent = Players[1];

        gameStatus = new GameStatus(Players[0].ClonePlayer(), Players[1].ClonePlayer());
    }

    public void setbNoPrint(boolean bNoPrint) { this.bNoPrint = bNoPrint; }

    public boolean isbMoveAgain() { return bMoveAgain; }

    public int getStonesCaptured() { return StonesCaptured; }

    public Player getCurrentMove() { return CurrentMove; }

    public void setStonesCaptured(int stonesCaptured) { StonesCaptured = stonesCaptured;}

    public GAME_STATUS getGameStatus(){
        return gameStatus.getGame_status();
    }

    public boolean IsGameOver(){
        return gameStatus.GameOver();
    }

    public void PrintGameBoard(Player CurrentPlayer){
        System.out.print("(" + CurrentPlayer.getOpponent().GetPlayerStorage() + ") ");
        CurrentPlayer.getOpponent().PrintBinsReverse();
        System.out.println();
        System.out.print("\t");
        CurrentPlayer.PrintBins();
        System.out.print(" (" + CurrentPlayer.GetPlayerStorage() + ")");
        System.out.println();
        System.out.print("Bins from left to right: ");
        for(int Counter = 0; Counter < BinNumber; Counter++){
            System.out.print((Counter+1) + " ");
        }
        System.out.println();
    }

    public MancalaBoard Clone(){
        MancalaBoard mancalaGame = new MancalaBoard();

        Player Current = CurrentMove.ClonePlayer();
        Player Oppo = Current.getOpponent();

        mancalaGame.Players[0] = (Current.getPlayerName().equals(this.Players[0].getPlayerName())) ? Current : Opponent;
        mancalaGame.Players[1] = mancalaGame.Players[0].getOpponent();
        mancalaGame.CurrentMove = Current;
        mancalaGame.Opponent = Oppo;

        mancalaGame.gameStatus = new GameStatus(mancalaGame.Players[0].ClonePlayer(), mancalaGame.Players[1].ClonePlayer());

        mancalaGame.setStonesCaptured(this.getStonesCaptured());
        mancalaGame.bMoveAgain = this.isbMoveAgain();
        mancalaGame.bSteal = this.bSteal;

        return mancalaGame;
    }

    public boolean isEqual(MancalaBoard mancalaGame){
        if(mancalaGame == null){
            return false;
        }
        if(mancalaGame == this){
            return true;
        }

        boolean bPlayer1 = CurrentMove.isEqual(mancalaGame.CurrentMove);
        boolean bPlayer2 = Opponent.isEqual(mancalaGame.Opponent);
        return bPlayer1 && bPlayer2;
    }

    private int GetMove(Player player){
        int BinChosen;
        if(player.getHeuristics() == E_HEURISTICS.H_HUMAN){
            Scanner scanner = new Scanner(System.in);
            int Move = scanner.nextInt();
            BinChosen = Move - 1;
        }else{
            MancalaBoard cloned = this.Clone();
            AI ai = new AI(player.ClonePlayer(), cloned);
            BinChosen = ai.GenerateMove();
            if(!bNoPrint){System.out.println("AI Chose " + (BinChosen + 1) + "\n");}
        }
        return BinChosen;
    }

    private void ApplyMove(Player Applier, int Bin){
        if(Applier.getStoneInBin(Bin) == 0){
            Player temp = CurrentMove;
            CurrentMove = Opponent;
            Opponent = temp;
            return;
        }
        StonesCaptured = 0;
        int StoneCount = Applier.getStoneInBin(Bin);
        Applier.setStoneInBin(Bin, 0);
        Player Current = Applier;
        int NextBin = Bin + 1;

        int[] PreviousBins = new int[BinNumber];
        for(int Counter=0; Counter < BinNumber; Counter++){
            PreviousBins[Counter] = Applier.getStoneInBin(Counter);
        }

        for(int Counter=0; Counter < StoneCount; Counter++){
            // Stone goes into Mancala if next bin is the mancala of the player
            if(NextBin == BinNumber && Current.getPlayerName().equals(Applier.getPlayerName())){
                Current.setMancalaCount(Current.getMancalaCount() + 1);
                StonesCaptured++;
                Current = Current.getOpponent();
                NextBin = 0;
                continue;
            }
            Current.setStoneInBin(NextBin,Current.getStoneInBin(NextBin) + 1);
            if(NextBin + 1 == BinNumber && !Current.getPlayerName().equals(Applier.getPlayerName())){
                NextBin = 0;
                Current = Current.getOpponent();
            }else {
                NextBin++;
            }
        }

        if(Applier.getPlayerName().equals(Players[0].getPlayerName())){
            gameStatus.UpdateStatus(Applier, Applier.getOpponent());
        }else{
            gameStatus.UpdateStatus(Applier.getOpponent(), Applier);
        }

        if(!gameStatus.GameOver()){
            bMoveAgain = NextBin == 0 && Current.getPlayerName().equals(Applier.getOpponent().getPlayerName());

            // Steal Mechanism
            // If Last Move Was On Player Side
            if(NextBin > 0 && Current.getPlayerName().equals(Applier.getPlayerName()) ){
                // If the last stone fell on an empty bin
                if(PreviousBins[NextBin-1] == 0){
                    // Steal
                    if(Applier.getOpponent().getStoneInBin(BinNumber -NextBin) != 0){
                        int StonesObtained = Applier.getStoneInBin(NextBin-1);
                        StonesObtained += Applier.getOpponent().getStoneInBin(BinNumber -NextBin);
                        Applier.setStoneInBin(NextBin-1, 0);
                        Applier.getOpponent().setStoneInBin(BinNumber - NextBin, 0);
                        StonesCaptured+=(StonesObtained);
                        Applier.setMancalaCount(Applier.getMancalaCount() + StonesObtained);
                        bSteal = true;

                        if(Applier.getPlayerName().equals(Players[0].getPlayerName())){
                            gameStatus.UpdateStatus(Applier, Applier.getOpponent());
                        }else{
                            gameStatus.UpdateStatus(Applier.getOpponent(), Applier);
                        }

                    }
                }
            }
        }

        if(!bMoveAgain){
            CurrentMove = Opponent;
            Opponent = Opponent.getOpponent();
        }
    }

    public MancalaBoard GenerateMoveSimulation(int Bin){
        MancalaBoard mancalaGame = this.Clone();
        mancalaGame.ApplyMove(mancalaGame.CurrentMove, Bin);
        return mancalaGame;
    }

    public MancalaBoard[] GenerateSuccessors(){
        MancalaBoard[] mancalaGames = new MancalaBoard[BinNumber];
        for(int Counter=0; Counter < BinNumber; Counter++){
            if(CurrentMove.getStoneInBin(Counter) > 0){
                mancalaGames[Counter] = GenerateMoveSimulation(Counter);
            }else{
                mancalaGames[Counter] = null;
            }
        }
        return mancalaGames;
    }

    public void GameLoop(){
        while (!gameStatus.GameOver()){
            if(!bNoPrint){
                PrintGameBoard(CurrentMove);

                if(bMoveAgain){
                    System.out.println(CurrentMove.getPlayerName() + " Moves Again");
                }
                System.out.println("Turn : " + CurrentMove.getPlayerName());
            }

            int Move = GetMove(CurrentMove);
            if(gameStatus.IllegalMove(Move, BinNumber, CurrentMove)){
                if(!bNoPrint){
                    System.out.println("Illegal move by " + CurrentMove.getPlayerName());
                }
                break;
            }

            ApplyMove(CurrentMove, Move);

            if(bSteal){
                if(!bNoPrint){
                    System.out.println("Steal by " + Opponent.getPlayerName() + "\n");
                }
                bSteal = false;
            }
            Opponent.setOpponent(CurrentMove);
        }

        if(!bNoPrint){
            switch (gameStatus.getGame_status()){
                case GAME_OVER_TIE -> System.out.println("Game Tied!");
                case GAME_OVER_MAX_WON -> System.out.println(Players[0].getPlayerName() + " Won!");
                case GAME_OVER_MIN_WON -> System.out.println(Players[1].getPlayerName() + " Won!");
            }
        }
    }
}
