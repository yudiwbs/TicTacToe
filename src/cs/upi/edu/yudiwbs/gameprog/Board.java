package cs.upi.edu.yudiwbs.gameprog;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by yudiwbs on 11/12/2015.
 */
public class Board {
    private int vCurrentPlayer=1;
    private boolean vIsGameOver = false;

    private int[] state  = new int[9];
    /*

        0 1 2
        3 4 5
        6 7 8

        isi: kalau 0 kosong, 1: player1, -1: player2

     */


    public int evaluasi (int player) {
        int skor=0;

        //kondisi menang dan skor maksimal
        /*
            //3 bebas

            x dapat berisi 1, 2 tertung parameter player

            idx player: 0,1,2, 3,4,5 dan 6,7,8
            x  x  x  :
            3  3  3
            3  3  3
            dst untuk semua baris


            idx player: 0,3,6   ; 1,4,7 ; 2,5,8
            x  3  3
            x  3  3
            x  3  3
            dst untuk semua kolom


            idx: 0, 4, 8;   2,4,6
            x  3  3:
            3  x  3
            3  3  x
            dst untuk semua diagonal

         */

        //kondisi kedua terpenting: blokir lawan (lawan adalah o)

        /*

        ref:
        0 1 2
        3 4 5
        6 7 8

            //implementasi  1 player1, -1 player2

            //idx: horizontal, blocking lawan
            -0, -1, 2 >> -0,1,-2 >> 0, -1, -2
            -3, -4, 5 >> -3,4,-5 >> 3 ,-4, -5
            -6, -7, 8 >> -6,7,-8 >> 6 , -7,-8

            o  o  x       o  x  o     x  o  o
            3  3  3       3  3  3     3  3  3
            3  3  3       3  3  3     3  3  3
            dst untuk semua baris

            idx: vertikal, blocking lawan
            -0,-3,6   >> -0,3,-6    >> 0,-3,-6
            -1,-4,7   >> -1,4,-7    >> 1,-4,-7
            -2,-5,8   >> -2,5,-8    >> 2,-5,-8

            o  3  3       o  3  3     x  3  3
            o  3  3       x  3  3     o  3  3
            x  3  3       o  3  3     o  3  3
            dst untuk semua kolom

            idx: diagonal, blocking lawan
            -0, -4, 8  => -0, 4, -8  => 0, -4, -8
            -2, -4, 6  =>  -2, 4, -6 => 2, -4, -6

            o  3  3       o  3  3    x  3  3
            3  o  3       3  x  3    3  o  3
            3  3  x       3  3  o    3  3  o
            dst untuk semua diagonal

         */
        /*   kondisi urutan ketiga terpenting: satu posisi menuju kemenangan  */
        /* ? artinya kosong

        ref:
        0 1 2
        3 4 5
        6 7 8


            menyerang (dua biji, tinggal satu lagi)
            posisi mengapit skornya lebih tinggi  (karena berpotensi menjebak)

            idx: horizontal: menyerang
            *: isinya nol

            0, *1, 2 >> 0,1,*2  >> *0, 1, 2
            3, *4, 5 >> 3,4,*5  >> *3, 4, 5
            6, *7, 8 >> 6,7,*8  >> *6, 7, 8

            x  ?  x       x  x  ?     ?  x  x
            3  3  3       3  3  3     3  3  3
            3  3  3       3  3  3     3  3  3
            dst untuk semua baris

            idx: vertical: meyerang
            *: isinya nol

            idx: vertikal, menyerang
            0,3,*6   >>  *0,3,6    >> 0,*3,6
            1,4,*7   >>  *1,4,7    >> 1,*4,7
            2,5,*8   >>  *2,5,8    >> 2,*5,8

            x  3  3       ?  3  3     x  3  3
            x  3  3       x  3  3     ?  3  3
            ?  3  3       x  3  3     x  3  3
            dst untuk semua kolom

            idx: diagonal, menyerang
            0, 4, *8  =>  0, *4, 8  => *0, 4, 8
            2, 4, *6  =>  2, *4, 6  => *2, 4, 6

            x  3  3       x  3  3    ?  3  3
            3  x  3       3  ?  3    3  x  3
            3  3  ?       3  3  x    3  3  x
            dst untuk semua diagonal

         */



        //=================================================> kondisi menang
        // idx player: 0,1,2, 3,4,5 dan 6,7,8  : horizontal
        // idx player: 0,3,6   ; 1,4,7 ; 2,5,8 : vertikal
        // idx: 0, 4, 8;   2,4,6               : diagonal

        //mungkin ada cara yg lebih efisien?
        //horizontal
        if (
                ((state[0]==player) && (state[1]==player) && (state[2]==player)) ||
                ((state[3]==player) && (state[4]==player) && (state[5]==player)) ||
                ((state[6]==player) && (state[7]==player) && (state[8]==player))

            )
        {
            skor = 1000;
            vIsGameOver = true;
        }

        //vertical
        if (
                ((state[0]==player) && (state[3]==player) && (state[6]==player)) ||
                ((state[1]==player) && (state[4]==player) && (state[7]==player)) ||
                ((state[2]==player) && (state[5]==player) && (state[8]==player))
           )
        {
            skor = 1000;
            vIsGameOver = true;
        }

        //diagonal
        if (
                ((state[0]==player) && (state[4]==player) && (state[8]==player)) ||
                ((state[2]==player) && (state[4]==player) && (state[6]==player))
                )
        {
            skor = 1000;
            vIsGameOver = true;
        }


        if  (!vIsGameOver)
        {
            //==========================> prioritas kedua, blokir lawan yang mau menang

            //horizontal
            //idx: horizontal, blocking lawan
            /*
                -0, -1, 2 >> -0,1,-2 >> 0, -1, -2
                -3, -4, 5 >> -3,4,-5 >> 3 ,-4, -5
                -6, -7, 8 >> -6,7,-8 >> 6 , -7,-8
            */
            if (
                 ((state[0] == -player) && (state[1]== -player) && (state[2]==player)) ||
                 ((state[3] == -player) && (state[4]== -player) && (state[5]==player)) ||
                 ((state[6] == -player) && (state[7]== -player) && (state[8]==player))
                    )
            {
                skor = skor + 80;
            }
            //baris 2
            if (
                    ((state[0] == -player) && (state[1]== player)  && (state[2]== -player)) ||
                    ((state[3] == -player) && (state[4]== player)  && (state[5]== -player)) ||
                    ((state[6] == -player) && (state[7]== player)  && (state[8]== -player))
                    )
            {
                skor = skor + 80;
            }
            //baris3
            if (
                    ((state[0]  == player) && (state[1]== -player)  && (state[2]== -player)) ||
                     ((state[3] == player) && (state[4]== -player)  && (state[5]== -player)) ||
                     ((state[6] == player) && (state[7]== -player)  && (state[8]== -player))
                    )
            {
                skor = skor + 80;
            }

            //idx: vertikal, blocking lawan
            //-0,-3,6   >> -0,3,-6    >> 0,-3,-6
            //-1,-4,7   >> -1,4,-7    >> 1,-4,-7
            //-2,-5,8   >> -2,5,-8    >> 2,-5,-8

            if (
                    ((state[0]== -player) && (state[3]== -player) && (state[6]==player)) ||
                    ((state[1]== -player) && (state[4]== -player) && (state[7]==player)) ||
                    ((state[2]== -player) && (state[5]== -player) && (state[8]==player))
                    )
            {
                skor = 80;
            }

            if (
                    ((state[0]== -player) && (state[3]== player) && (state[6]== -player)) ||
                    ((state[1]== -player) && (state[4]== player) && (state[7]== -player)) ||
                    ((state[2]== -player) && (state[5]== player) && (state[8]== -player))
                    )
            {
                skor = 80;
            }

            if (
                    ((state[0]  == player) && (state[3]== -player) && (state[6]== -player)) ||
                     ((state[1] == player) && (state[4]== -player) && (state[7]== -player)) ||
                     ((state[2] == player) && (state[5]== -player) && (state[8]== -player))
                    )
            {
                skor = 80;
            }

            /*  idx: diagonal, blocking lawan
                -0, -4, 8  => -0, 4, -8  => 0, -4, -8
                -2, -4, 6  =>  -2, 4, -6 => 2, -4, -6
            */

            if (
                    ((state[0]== -player) && (state[4]== -player) && (state[8]==player)) ||
                    ((state[2]== -player) && (state[4]== -player) && (state[6]==player))
                    )
            {
                skor = 80;
            }

            if (
                    ((state[0]== -player) && (state[4]== player) && (state[8]== -player)) ||
                    ((state[2]== -player) && (state[4]== player) && (state[6]== -player))
                    )
            {
                skor = 80;
            }

            if (
                    ((state[0]== player) && (state[4]== -player) && (state[8]== -player)) ||
                    ((state[2]== player) && (state[4]== -player) && (state[6]== -player))
                    )
            {
                skor = 80;
            }


            //==========================> prioritas ketiga, serang (dua biji)

            //horizontal
            /*
            idx: horizontal: menyerang
            *: isinya nol

            0, *1, 2 >> 0,1,*2  >> *0, 1, 2
            3, *4, 5 >> 3,4,*5  >> *3, 4, 5
            6, *7, 8 >> 6,7,*8  >> *6, 7, 8

            */

            if (
                    ((state[0] == player) && (state[1]== 0) && (state[2]==player)) ||
                    ((state[3] == player) && (state[4]== 0) && (state[5]==player)) ||
                    ((state[6] == player) && (state[7]== 0) && (state[8]==player))
                    )
            {
                skor = skor + 50;  //mengapit, skor lebih tinggi
            }

            if (
                    ((state[0] == player) && (state[1]== player)  && (state[2]== 0)) ||
                    ((state[3] == player) && (state[4]== player)  && (state[5]== 0)) ||
                    ((state[6] == player) && (state[7]== player)  && (state[8]== 0))
                    )
            {
                skor = skor + 30;
            }

            if (
                    ((state[0] == 0) && (state[1]== player)  && (state[2]== player)) ||
                    ((state[3] == 0) && (state[4]== player)  && (state[5]== player)) ||
                    ((state[6] == 0) && (state[7]== player)  && (state[8]== player))
                    )
            {
                skor = skor + 30;
            }

            //idx: vertikal
            /*
            idx: vertikal, menyerang
            0,3,*6   >>  *0,3,6    >> 0,*3,6
            1,4,*7   >>  *1,4,7    >> 1,*4,7
            2,5,*8   >>  *2,5,8    >> 2,*5,8
            */


            if (
                     ((state[0]== player) && (state[3]== player) && (state[6]==0)) ||
                     ((state[1]== player) && (state[4]== player) && (state[7]==0)) ||
                     ((state[2]== player) && (state[5]== player) && (state[8]==0))
                )
            {
                skor = 30;
            }

            if (
                    ((state[0]== 0) && (state[3]== player) && (state[6]== player)) ||
                    ((state[1]== 0) && (state[4]== player) && (state[7]== player)) ||
                    ((state[2]== 0) && (state[5]== player) && (state[8]== player))
                )
            {
                skor = 30;
            }

            if (
                    ((state[0] == player) && (state[3]== 0) && (state[6]== player)) ||
                    ((state[1] == player) && (state[4]== 0) && (state[7]== player)) ||
                    ((state[2] == player) && (state[5]== 0) && (state[8]== player))
                    )
            {
                skor = 50;
            }

            /*  idx: diagonal

            idx: diagonal, menyerang
            0, 4, *8  =>  0, *4, 8  => *0, 4, 8
            2, 4, *6  =>  2, *4, 6  => *2, 4, 6


            */

            if (
                    ((state[0]== player) && (state[4]== player) && (state[8]==0)) ||
                    ((state[2]== player) && (state[4]== player) && (state[6]==0))
                    )
            {
                skor = 30;
            }

            if (
                    ((state[0]== player) && (state[4]== 0) && (state[8]== player)) ||
                    ((state[2]== player) && (state[4]== 0) && (state[6]== player))
                    )
            {
                skor = 50; //lebih besar menjepit
            }

            if (
                    ((state[0]== 0) && (state[4]== player) && (state[8]== player)) ||
                    ((state[2]== 0) && (state[4]== player) && (state[6]== player))
                )
            {
                skor = 30;
            }
        }  //if !gameover
        return skor;
    }

    public boolean isGameOver() {
        return vIsGameOver;
    }

    public int currentPlayer() {
        return vCurrentPlayer;
    }

    /*move:

      0  1  2
      3  4  5
      6  7  8

     */

    public void makeMove(int m) {
        state[m] =  vCurrentPlayer;
        vCurrentPlayer = -vCurrentPlayer;  //ganti player
    }

    public ArrayList<Integer> getMoves() {
    /*
        menghasilkan move yang mungkin
    */

        ArrayList<Integer> temp  = new ArrayList<>();
        return temp;
    }


}
