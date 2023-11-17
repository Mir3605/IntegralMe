package com.example.integralmefirst.gameshistory;

import java.util.ArrayList;

public class GameData {
    private final ArrayList<String> problems;
    private final int points;
    private final ArrayList<Long> times;
    private final long date;

    public ArrayList<String> getProblems() {
        return problems;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<Long> getTimes() {
        return times;
    }

    public long getDates() {
        return date;
    }

    public GameData(ArrayList<String> problems, int points, ArrayList<Long> times, long date) {
        this.problems = problems;
        this.points = points;
        this.times = times;
        this.date = date;
    }
}
