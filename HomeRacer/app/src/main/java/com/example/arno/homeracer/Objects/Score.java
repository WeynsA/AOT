package com.example.arno.homeracer.Objects;

import java.io.Serializable;

public class Score implements Serializable {
    private Long Time;
    private String UsernameHS;
    private Boolean HomeRace;

    public Long getTime(){
        return Time;
    }

    public String getUsernameHS() {
        return UsernameHS;
    }

    public Boolean getHomeRace() {
        return HomeRace;
    }

    public Score(String usernameHS, Long time, boolean homeRace){
        this.Time = time;
        this.UsernameHS = usernameHS;
        this.HomeRace = homeRace;
    }
}
