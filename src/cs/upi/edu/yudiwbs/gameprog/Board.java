package cs.upi.edu.yudiwbs.gameprog;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by yudiwbs on 11/12/2015.
 *
 *  menyimpan board, player pada board, evaluasi posisi dst.
 *
 * todo: evaluasinya masih sangat tidak efisien, ternyata dengan rumus bisa, googling saja
 *       ini saya buat dengan mencoba tidak googling :)
 *
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


    public void setCurrentPlayer(int cp) {
        vCurrentPlayer = cp;
    }

    public void print() {  //untuk debug, print board
      System.out.println(); //spasi
      for (int i=0; i<9; i=i+3) {
          System.out.println(String.format("%3d%3d%3d",state[i],state[i+1],state[i+2]) );
      }
      System.out.println();
    }

    public ArrayList<Integer> getMoves() {
        //mengembalikan move yang mungkin
        //idx state yang statenya masih 0 (belum terisi 1 atau -1)
        //init board
        ArrayList<Integer> out = new ArrayList<>();
        for (int i=0;i<9;i++) {
            if (state[i]==0) {
                out.add(i);
            }
        }
        return out;
    }


    //constructor
    public Board() {
        //init board
        for (int i:state) {
            state[i]=0;
        }
    }

    public boolean isGameOver() {
        return vIsGameOver;
    }

    public int currentPlayer() {
        return vCurrentPlayer;
    }


    public void copyTo(Board b) {
        //isi this.state ke b.state
        System.arraycopy(this.state, 0, b.state, 0, 9);
    }

    /*move:

      0  1  2
      3  4  5
      6  7  8

     */

    //menghasilkan board yang isinya copy dari board ini, ditambah dengan move
    //current player juga diganti di board yang baru
    //penting: deteksi jika game berakhir
    public Board  makeMove(int m) {
        Board out = new Board();
        this.copyTo(out);    //copy state board ini ke board yang baru
        out.state[m]       =  this.currentPlayer();  //gerakan player
        out.vCurrentPlayer = -this.currentPlayer();  //gantian

        //System.out.println("debug makemove");
        //out.print();

        //cek apakah game over, sudah tidak ada langkah lagi, cara yg lebih efisien mungkin pake variabel, tapi untuk sementara ok lah


        int player = this.currentPlayer();
        //cek kondisi menang
        if (
                ((out.state[0]==player) && (out.state[1]==player) && (out.state[2]==player)) ||
                ((out.state[3]==player) && (out.state[4]==player) && (out.state[5]==player)) ||
                ((out.state[6]==player) && (out.state[7]==player) && (out.state[8]==player))
                )
        {
            out.vIsGameOver = true;
        }

        //vertical
        if (
                ((out.state[0]==player) && (out.state[3]==player) && (out.state[6]==player)) ||
                ((out.state[1]==player) && (out.state[4]==player) && (out.state[7]==player)) ||
                ((out.state[2]==player) && (out.state[5]==player) && (out.state[8]==player))
                )
        {
            out.vIsGameOver = true;
        }

        //diagonal
        if (
                ((out.state[0]==player) && (out.state[4]==player) && (out.state[8]==player)) ||
                ((out.state[2]==player) && (out.state[4]==player) && (out.state[6]==player))
                )
        {
            out.vIsGameOver = true;
        }

        boolean masihAdaLangkah=false;
        for (int i=0;i<9;i++) {
            if (out.state[i]==0) {

                masihAdaLangkah = true;
                break;
            }
        }
        if (!masihAdaLangkah) {
            out.vIsGameOver = true;
        }

        return out;
    }


    //harusnya ada cara yg lebih efisien,
    //percoban tanpa lihat internet :)
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
            skor = skor +  1000;

        }

        //vertical
        if (
                ((state[0]==player) && (state[3]==player) && (state[6]==player)) ||
                ((state[1]==player) && (state[4]==player) && (state[7]==player)) ||
                ((state[2]==player) && (state[5]==player) && (state[8]==player))
           )
        {
            skor = skor + 1000;
        }

        //diagonal
        if (
                ((state[0]==player) && (state[4]==player) && (state[8]==player)) ||
                ((state[2]==player) && (state[4]==player) && (state[6]==player))
                )
        {
            skor = skor +  1000;
        }



            //==========================> kondisi tidak menguntungkan, ada dua biji lawan berurutan

            //horizontal
            //idx: horizontal,  lawan
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
                skor = skor + 150;
            }

            if (
                    ((state[0] == -player) && (state[1]== player)  && (state[2]== -player)) ||
                    ((state[3] == -player) && (state[4]== player)  && (state[5]== -player)) ||
                    ((state[6] == -player) && (state[7]== player)  && (state[8]== -player))
                    )
            {
                skor = skor + 150;
            }

            if (
                    ((state[0]  == player) && (state[1]== -player)  && (state[2]== -player)) ||
                     ((state[3] == player) && (state[4]== -player)  && (state[5]== -player)) ||
                     ((state[6] == player) && (state[7]== -player)  && (state[8]== -player))
                    )
            {
                skor = skor + 150;
            }

            //idx: vertikal,
            //-0,-3,6   >> -0,3,-6    >> 0,-3,-6
            //-1,-4,7   >> -1,4,-7    >> 1,-4,-7
            //-2,-5,8   >> -2,5,-8    >> 2,-5,-8

            if (
                    ((state[0]== -player) && (state[3]== -player) && (state[6]==player)) ||
                    ((state[1]== -player) && (state[4]== -player) && (state[7]==player)) ||
                    ((state[2]== -player) && (state[5]== -player) && (state[8]==player))
                    )
            {
                skor = skor + 150;
            }

            if (
                    ((state[0]== -player) && (state[3]== player) && (state[6]== -player)) ||
                    ((state[1]== -player) && (state[4]== player) && (state[7]== -player)) ||
                    ((state[2]== -player) && (state[5]== player) && (state[8]== -player))
                    )
            {
                skor = skor + 150;
            }

            if (
                    ((state[0]  == player) && (state[3]== -player) && (state[6]== -player)) ||
                     ((state[1] == player) && (state[4]== -player) && (state[7]== -player)) ||
                     ((state[2] == player) && (state[5]== -player) && (state[8]== -player))
                    )
            {
                skor = skor + 150;
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
                skor = skor + 150;
            }

            if (
                    ((state[0]== -player) && (state[4]== player) && (state[8]== -player)) ||
                    ((state[2]== -player) && (state[4]== player) && (state[6]== -player))
                    )
            {
                skor =skor + 150;
            }

            if (
                    ((state[0]== player) && (state[4]== -player) && (state[8]== -player)) ||
                    ((state[2]== player) && (state[4]== -player) && (state[6]== -player))
                    )
            {
                skor =skor + 150;
            }


            //==========================> posisi menguntungkan, (dua biji berurutan)

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
                skor = skor + 30;
            }

            if (
                    ((state[0]== 0) && (state[3]== player) && (state[6]== player)) ||
                    ((state[1]== 0) && (state[4]== player) && (state[7]== player)) ||
                    ((state[2]== 0) && (state[5]== player) && (state[8]== player))
                )
            {
                skor = skor +  30;
            }

            if (
                    ((state[0] == player) && (state[3]== 0) && (state[6]== player)) ||
                    ((state[1] == player) && (state[4]== 0) && (state[7]== player)) ||
                    ((state[2] == player) && (state[5]== 0) && (state[8]== player))
                    )
            {
                skor = skor + 50;
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
                skor = skor +  30;
            }

            if (
                    ((state[0]== player) && (state[4]== 0) && (state[8]== player)) ||
                    ((state[2]== player) && (state[4]== 0) && (state[6]== player))
                    )
            {
                skor = skor +  50; //lebih besar menjepit
            }

            if (
                    ((state[0]== 0) && (state[4]== player) && (state[8]== player)) ||
                    ((state[2]== 0) && (state[4]== player) && (state[6]== player))
                )
            {
                skor = skor +  30;
            }
        return skor;
    }




}
