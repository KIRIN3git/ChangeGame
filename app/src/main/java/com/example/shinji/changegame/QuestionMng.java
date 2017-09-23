package com.example.shinji.changegame;

/**
 * Created by etisu on 2017/09/23.
 */

public class QuestionMng {

    static int sPointNum = 0;

    static int NewQuestion(){

        int yen = (int)Math.random()*30 + 1;

        return  yen;
    }
}
