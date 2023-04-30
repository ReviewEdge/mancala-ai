import java.util.Arrays;

public class Player {
    private final E_HEURISTICS heuristics;
    private final int[] Bins;
    private int MancalaCount;
    private final String PlayerName;

    private Player Opponent;

    private final Heuristics PHeuristic;

    public Player(E_HEURISTICS heuristics, int BinCount, int InitialStones, String playerName) {
        this.heuristics = heuristics;
        MancalaCount = 0;
        Bins = new int[BinCount];
        for(int Counter = 0; Counter < BinCount; Counter++){
            Bins[Counter] = InitialStones;
        }
        PlayerName = playerName;
        PHeuristic = new Heuristics(heuristics);
    }

    public Player(E_HEURISTICS heuristics, int[] bins, int MancalaCount, String playerName){
        this.heuristics = heuristics;
        this.Bins = new int[bins.length];
        this.MancalaCount = MancalaCount;
        this.PlayerName = playerName;
        this.PHeuristic = new Heuristics(this.heuristics);
        System.arraycopy(bins, 0, this.Bins, 0, bins.length);
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public E_HEURISTICS getHeuristics() {
        return heuristics;
    }

    public Player getOpponent() {
        return Opponent;
    }

    public int getStoneInBin(int Bin){
        return Bins[Bin];
    }

    public int[] getBins(){
        return Bins;
    }

    public void setStoneInBin(int Bin, int Stone){
        Bins[Bin] = Stone;
    }

    public void setOpponent(Player opponent) {
        Opponent = opponent;
    }

    public int getMancalaCount() {
        return MancalaCount;
    }

    public void setMancalaCount(int mancalaCount) {
        MancalaCount = mancalaCount;
    }

    public int GetTotalStonesInBins(){
        int Stones = 0;
        for (int bin : Bins) {
            Stones += bin;
        }
        return Stones;
    }

    public int GetPlayerStorage(){
        return MancalaCount;
    }

    public void SetOpponent(Player opponent){
        Opponent = opponent;
    }

    public Player ClonePlayer(){
        Player player = new Player(heuristics, Bins, MancalaCount, this.PlayerName);
        Player opponent = new Player(this.Opponent.heuristics, this.Opponent.Bins, this.Opponent.MancalaCount, this.Opponent.PlayerName);
        opponent.Opponent = player;
        player.SetOpponent(opponent);
        return player;
    }

    public void PrintBins(){
        for(int Counter = 0; Counter < Bins.length; Counter++){
            if(Counter == Bins.length-1){
                System.out.print(Bins[Counter]);
            }else{
                System.out.print(Bins[Counter] + " ");
            }
        }
    }

    public void PrintBinsReverse(){
        for(int Counter = Bins.length - 1; Counter >= 0; Counter--){
            if(Counter == 0){
                System.out.print(Bins[Counter]);
            }else{
                System.out.print(Bins[Counter] + " ");
            }
        }
    }

    public boolean isEqual(Player player){
        if(player == null){
            return false;
        }
        if(player == this){
            return true;
        }

        boolean bBins = Arrays.equals(this.Bins, player.Bins);
        boolean bMancala = this.MancalaCount == player.MancalaCount;
        boolean bName = this.PlayerName.equals(player.getPlayerName());
        return bBins && bMancala && bName;
    }

    public int GetHeuristicValue(MancalaBoard mancalaGame){
        PHeuristic.setPlayerBins(this.Bins);
        PHeuristic.setOpponentBins(this.Opponent.Bins);
        PHeuristic.setPlayerMancala(this.MancalaCount);
        PHeuristic.setOpponentMancala(this.Opponent.MancalaCount);
        PHeuristic.setbChainedMove(mancalaGame.isbMoveAgain());
        return PHeuristic.GetHeuristicsValue();
    }
}
