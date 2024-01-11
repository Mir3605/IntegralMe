package com.example.integralmefirst.mainmenu;

import androidx.annotation.NonNull;

public class Lvl {
    private final int difficulty;

    @NonNull
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
