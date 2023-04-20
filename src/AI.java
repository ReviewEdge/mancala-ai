import java.util.Random;

class SearchResult{
    private final MancalaBoard mancalaGame;
    private final int HeuristicValue;

    public SearchResult(MancalaBoard mancalaGame, int heuristicValue) {
        this.mancalaGame = mancalaGame;
        HeuristicValue = heuristicValue;
    }

    public MancalaBoard getMancalaGame() {
        return mancalaGame;
    }

    public int getHeuristicValue() {
        return HeuristicValue;
    }
}

public class AI {
    private final Player AIPlayer;
    private final MancalaBoard CurrentStatus;

    public AI(Player AIPlayer, MancalaBoard mancalaGame) {
        this.AIPlayer = AIPlayer;
        CurrentStatus = mancalaGame;
    }

    public int GenerateMove(){
        MancalaBoard Cloned = CurrentStatus.Clone();
        MancalaBoard[] NextStates = Cloned.GenerateSuccessors();

        MancalaBoard Cloned2 = CurrentStatus.Clone();
        Cloned2.setStonesCaptured(CurrentStatus.getStonesCaptured());

        SearchResult Next = AlphaBeta(Cloned2, MancalaGame.NEG_INFINITY, MancalaGame.INFINITY, true, MancalaGame.MAX_DEPTH);

        for(int Counter = 0; Counter < NextStates.length; Counter++){
            if(NextStates[Counter] != null){
                if(NextStates[Counter].isEqual(Next.getMancalaGame())){
                    return Counter;
                }
            }
        }

        Next.getMancalaGame().PrintGameBoard(Next.getMancalaGame().getCurrentMove());
        return -1;
    }

    private SearchResult AlphaBeta(MancalaBoard mancalaGame, int Alpha, int Beta, boolean bMaximizing, int Depth){

        if(mancalaGame.IsGameOver() || Depth == 0){
            MancalaBoard Cloned = mancalaGame.Clone();
            Cloned.setStonesCaptured(mancalaGame.getStonesCaptured());
            return new SearchResult(mancalaGame, mancalaGame.getCurrentMove().GetHeuristicValue(Cloned));
        }
        // Player move => Maximize
        if(bMaximizing){
            SearchResult MaxSearchResult = new SearchResult(null, MancalaGame.NEG_INFINITY);
            MancalaBoard[] NextStates = mancalaGame.GenerateSuccessors();

            for(MancalaBoard game : NextStates){
                if(game != null){
                    MancalaBoard cloned = game.Clone();
                    cloned.setStonesCaptured(game.getStonesCaptured());
                    SearchResult searchResult = AlphaBeta(cloned, Alpha, Beta, game.getCurrentMove().getPlayerName().equals(AIPlayer.getPlayerName()), Depth - 1);
                    SearchResult probableMax = new SearchResult(game, searchResult.getHeuristicValue());

                    MaxSearchResult = GetMaxResult(probableMax, MaxSearchResult);
                    Alpha = Math.max(Alpha, MaxSearchResult.getHeuristicValue());

                    //Prunning
                    if(Beta <= Alpha){
                        break;
                    }
                }
            }
            return MaxSearchResult;
        }
        // Opponent move => Minimize
        else{
            SearchResult MinSearchResult = new SearchResult(null, MancalaGame.INFINITY);
            MancalaBoard[] NextStates = mancalaGame.GenerateSuccessors();

            for(MancalaBoard game : NextStates){
                if(game != null){
                    MancalaBoard cloned = game.Clone();
                    cloned.setStonesCaptured(game.getStonesCaptured());
                    SearchResult searchResult = AlphaBeta(cloned, Alpha, Beta, game.getCurrentMove().getPlayerName().equals(AIPlayer.getPlayerName()), Depth - 1);
                    SearchResult probableMin = new SearchResult(game, searchResult.getHeuristicValue());

                    MinSearchResult = GetMinResult(probableMin, MinSearchResult);
                    Beta = Math.min(Beta, MinSearchResult.getHeuristicValue());

                    //Prunning
                    if(Beta <= Alpha){
                        break;
                    }
                }
            }
            return MinSearchResult;
        }
    }

    private SearchResult GetMaxResult(SearchResult searchResult1, SearchResult searchResult2){
        return getSearchResultUtil(searchResult1, searchResult2, searchResult1.getHeuristicValue() > searchResult2.getHeuristicValue());
    }

    private SearchResult GetMinResult(SearchResult searchResult1, SearchResult searchResult2){
        return getSearchResultUtil(searchResult1, searchResult2, searchResult1.getHeuristicValue() < searchResult2.getHeuristicValue());
    }

    private SearchResult getSearchResultUtil(SearchResult searchResult1, SearchResult searchResult2, boolean b) {
        if(searchResult1 == null){
            return searchResult2;
        }
        if(searchResult2 == null){
            return searchResult1;
        }
        if(searchResult1.getMancalaGame() == null){
            return searchResult2;
        }
        if(searchResult2.getMancalaGame() == null){
            return searchResult1;
        }
        if(b){
            return searchResult1;
        }else if(searchResult1.getHeuristicValue() == searchResult2.getHeuristicValue()){
            Random random = new Random();
            int rand = random.nextInt() % 2;
            return (rand == 0) ? searchResult1 : searchResult2;
        } else{
            return searchResult2;
        }
    }
}
