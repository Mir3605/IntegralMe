package com.example.integralmefirst;

public class Lvl {
    private int difficulty;

    @Override
    public String toString() {
        return "Level " + difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Lvl(int difficulty) {
        this.difficulty = difficulty;
    }
}
