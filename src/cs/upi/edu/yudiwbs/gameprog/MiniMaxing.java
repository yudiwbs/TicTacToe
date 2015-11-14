package cs.upi.edu.yudiwbs.gameprog;

/**
 * Created by yudiwbs on 11/12/2015.
 * tictactoe dengan minimaxing
 */

public class MiniMaxing {

    public int proses(Board mBoard,int player, int maxDepth, int currentDepth ) {
        int out=0;

        //kondisi berhenti
        if (mBoard.isGameOver() || currentDepth >= maxDepth ) {
            out = mBoard.evaluasi(player);
        }


        return out;
    }

    public static void main(String[] args) {

    }

}
