package com.example.arno.homeracer;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Score> {
    public int compare(Score score1, Score score2) {

        Long sc1 = score1.getTime();
        Long sc2 = score2.getTime();

        if (sc1 > sc2)
            return -1;
        else if (sc1 < sc2)
            return +1;
        else
            return 0;
    }
}
