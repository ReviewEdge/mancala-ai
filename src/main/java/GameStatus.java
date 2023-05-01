enum GAME_STATUS{
    GAME_RUNNING,
    GAME_OVER_TIE,
    GAME_OVER_MIN_WON,
    GAME_OVER_MAX_WON
}

public class GameStatus {
    private Player MaxPlayer;
    private Player MinPlayer;

    private GAME_STATUS game_status = GAME_STATUS.GAME_RUNNING;

    public GameStatus(Player maxPlayer, Player minPlayer) {
        MaxPlayer = maxPlayer;
        MinPlayer = minPlayer;
    }

    public GAME_STATUS getGame_status() {
        return game_status;
    }

    public boolean GameOver(){
        return game_status != GAME_STATUS.GAME_RUNNING;
    }

    public boolean IllegalMove(int Move, int Bins, Player Mover){
        boolean Illegal = false;
        if(Move < 0 || Move >= Bins){
            Illegal = true;
            if(Mover.getPlayerName().equals(MaxPlayer.getPlayerName())){
                game_status = GAME_STATUS.GAME_OVER_MIN_WON;
            }else{
                game_status = GAME_STATUS.GAME_OVER_MAX_WON;
            }
        }
        return Illegal;
    }

    public void UpdateStatus(Player Max, Player Min){
        if(Max.GetTotalStonesInBins() == 0 || Min.GetTotalStonesInBins() == 0){
            if(Max.GetTotalStonesInBins() + Max.getMancalaCount() > Min.GetTotalStonesInBins() + Min.getMancalaCount()){
                game_status = GAME_STATUS.GAME_OVER_MAX_WON;
            }else if(Max.GetTotalStonesInBins() + Max.getMancalaCount() < Min.GetTotalStonesInBins() + Min.getMancalaCount()){
                game_status = GAME_STATUS.GAME_OVER_MIN_WON;
            }else if(Max.GetTotalStonesInBins() + Max.getMancalaCount() == Min.GetTotalStonesInBins() + Min.getMancalaCount()){
                game_status = GAME_STATUS.GAME_OVER_TIE;
            }
        }else{
            game_status = GAME_STATUS.GAME_RUNNING;
        }
    }
}
