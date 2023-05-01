public class Heuristics {
    private final E_HEURISTICS heuristics;
    private int[] PlayerBins;
    private int[] OpponentBins;
    private int PlayerMancala;
    private int OpponentMancala;
    private boolean bChainedMove;

    public static int W1 = 12;
    public static int W2 = 6;
    public static int W3 = 16;
    public static int W4 = 13;
    public static int W5 = 14;
    public static int W6 = 13;

    public Heuristics(E_HEURISTICS heuristics) {
        this.heuristics = heuristics;
    }

    public void setPlayerBins(int[] playerBins) {
        PlayerBins = playerBins;
    }

    public void setOpponentBins(int[] opponentBins) {
        OpponentBins = opponentBins;
    }

    public void setPlayerMancala(int playerMancala) {
        PlayerMancala = playerMancala;
    }

    public void setOpponentMancala(int opponentMancala) {
        OpponentMancala = opponentMancala;
    }

    public void setbChainedMove(boolean bChainedMove) {
        this.bChainedMove = bChainedMove;
    }

    public int GetHeuristicsValue(){
        switch (heuristics){
            case H_STORAGE_DIFFERENCE -> {
                return StorageDifference();
            }
            case H_STONE_AND_STORAGE -> {
                return StorageAndStone();
            }
            case H_CHAIN_MOVES_PRIORITIZE -> {
                return ChainMoves();
            }
            case H_STONES_CLOSE_TO_MY_STORAGE -> {
                return StonesCloseToMyStorage();
            }
            case H_REDUCE_OPPONENT_POINT -> {
                return ReduceOpponentPoint();
            }
            case H_CLOSE_TO_WINNING -> {
                return CloseToWinning();
            }
            default -> {
                return MancalaGame.NEG_INFINITY;
            }
        }
    }

    private int StorageDifference(){
        return PlayerMancala - OpponentMancala;
    }

    private int StorageAndStone(){
        int PlayerStones = GetTotalStonesInBins(PlayerBins);
        int OpponentStones = GetTotalStonesInBins(OpponentBins);
        int StorageDiff = StorageDifference();
        return W1 * StorageDiff + W2 * (PlayerStones - OpponentStones);
    }

    private int ChainMoves(){
        int ChainMoveGained = bChainedMove ? 1 : 0;
        return StorageAndStone() + W3 * ChainMoveGained;
    }

    private int StonesCloseToMyStorage(){
        int Stones = StonesClose(PlayerBins);
        return ChainMoves() + W4 * Stones;
    }

    private int CloseToWinning(){
        int TotalStones = GetTotalStonesInBins(PlayerBins) + GetTotalStonesInBins(OpponentBins) + PlayerMancala + OpponentMancala;
        int CloseValue = PlayerMancala - (TotalStones/2);
        return StonesCloseToMyStorage() + W5 * CloseValue;
    }

    private int ReduceOpponentPoint(){
        return CloseToWinning() - W6 * OpponentMancala;
    }

    private int GetTotalStonesInBins(int[] Bins){
        int Stones = 0;
        for (int bin : Bins) {
            Stones += bin;
        }
        return Stones;
    }

    private int StonesClose(int[] Bins){
        int Count = 0;
        int TotalBins = Bins.length;
        for(int Counter = 0; Counter < TotalBins; Counter++){
            if(Bins[Counter] == TotalBins - Counter){
                Count++;
            }else{
                int NewMoveAgain = TotalBins * 2 + 2;
                int Turns = Bins[Counter]/NewMoveAgain;
                if((Bins[Counter] + Turns)%NewMoveAgain == 0){
                    Count++;
                }
            }
        }
        return Count;
    }
}
