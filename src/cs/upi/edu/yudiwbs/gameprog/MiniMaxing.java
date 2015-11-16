package cs.upi.edu.yudiwbs.gameprog;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yudiwbs on 11/12/2015.
 * tictactoe dengan minimaxing dan negamax
 * untuk kuliah gameprog
 */

public class MiniMaxing {

    static final int MAXDEPTH = 8;

    static final int INFINITY = Integer.MAX_VALUE;

    public MoveScore minimax(Board mBoard,int player,int currentDepth ) {
        MoveScore best = new MoveScore();
        //kondisi berhenti, gameover atau sudah terlalu dalam
        if (mBoard.isGameOver() || currentDepth == MAXDEPTH ) {
            best.score = mBoard.evaluasi(player);
            best.move = -1; //tidak ada move
        } else
        {
            best.move = -1;
            if (mBoard.currentPlayer() == player)
                best.score = -INFINITY;  //cari maks
            else
                best.score = INFINITY;   //cari min

            //loop untuk semua kemungkinan gerakan
            //for move in board.getMoves():

            //System.out.println("debug mboard");
            //mBoard.print();
            ArrayList<Integer> alMoves = mBoard.getMoves();
            for (int move:alMoves) {
                Board newBoard = mBoard.makeMove(move);  //buat board yg baru, isinya sama dgn yg lama + move

                //System.out.println("debug new board:");
                //newBoard.print();

                //currentScore, currentMove = minimax(newBoard, player,20 maxDepth, currentDepth+1)
                MoveScore currentMS = minimax(newBoard,player,currentDepth+1); //rekursif

                if (mBoard.currentPlayer() == player) {
                    if  (currentMS.score > best.score) {
                        best.score = currentMS.score;
                        best.move = move;
                    }
                } else  //lawan, cari minimal
                {
                    if (currentMS.score < best.score) {
                        best.score =currentMS.score;
                        best.move = move;
                    }
                }
            }
        }
        //debug
        /*
        System.out.print("Best score:  ");
        System.out.println(best.score);
        System.out.print("Best Move:  ");
        System.out.println(best.move);
        */
        return best;
    }

    public MoveScore getBestMove(Board mBoard,int player) {
        MoveScore out;
        out  = minimax(mBoard, player,0);
        return out;
    }

    public static void main(String[] args) {
        MiniMaxing mm = new MiniMaxing();
        Board b = new Board();

        /*
        //debug satu langkah
        b.setCurrentPlayer(1);
        b = b.makeMove(4);  //debug  player 1 main
        b.print();
        MoveScore ms = mm.getBestMove(b,1);
        ms.print();
        System.out.println("selesai");
        */

        b.setCurrentPlayer(1);
        Scanner keyboard = new Scanner(System.in);
        while (!b.isGameOver()) {
            b.print();
            System.out.println("Langkah anda ?");
            System.out.println("0  1  2");
            System.out.println("3  4  5");
            System.out.println("6  7  8");
            int pil = keyboard.nextInt();
            System.out.println("Pilihan anda"+pil);
            b = b.makeMove(pil);  //player gerak . Hati2 memory leaak, pastikan tdk ada ref ke b yg lama
            b.print();
            if (!b.isGameOver()) {
                System.out.println("giliran komputer..");
                MoveScore ms = mm.getBestMove(b, -1);
                System.out.println("Pilihan komputer.." + ms.move);
                b = b.makeMove(ms.move);  //komputer gerak4
                b.print();
            }
            //todo: ada pengecekan, tidak boleh melangkah di tempat yang sudah ditempati
        }
    }

}
