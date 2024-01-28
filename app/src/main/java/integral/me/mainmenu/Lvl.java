package integral.me.mainmenu;

import android.content.Context;

import androidx.annotation.NonNull;

import integral.me.R;

public class Lvl {
    private final int difficulty;
    private final Context context;

    @NonNull
    @Override
    public String toString() {
        return context.getString(R.string.level) + " " + difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Lvl(Context context, int difficulty) {
        this.difficulty = difficulty;
        this.context = context;
    }
}
