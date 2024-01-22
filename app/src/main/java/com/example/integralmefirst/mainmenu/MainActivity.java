package com.example.integralmefirst.mainmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;
import com.example.integralmefirst.gameshistory.GamesHistoryActivity;
import com.example.integralmefirst.settings.Settings;
import com.example.integralmefirst.settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int difficultyLevelsNumber = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper helper = new DBHelper(this); // DBHelper should be initialized at the beginning of the program
        Settings.readSettingsFromDB();

        RecyclerView chooseLvlRecView = findViewById(R.id.ChooseLvlRecView);
        ArrayList<Lvl> lvlsArray = new ArrayList<>();
        for (int i = 0; i < difficultyLevelsNumber; i++) {
            lvlsArray.add(new Lvl(i));
        }
        LevelsRecViewAdapter adapter = new LevelsRecViewAdapter(this);
        adapter.setLvls(lvlsArray);
        chooseLvlRecView.setAdapter(adapter);
        chooseLvlRecView.setLayoutManager(new LinearLayoutManager(this));

        Button gamesHistoryButton = findViewById(R.id.GamesHistoryButton);
        gamesHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToGamesHistory();
            }
        });
        Button settingsButton = findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSettings();
            }
        });

        if (Settings.getDisplayTutorial()) {
            // TODO Move to tutorial section
            // something.bringToFront();
        }
    }

    private void switchToGamesHistory() {
        Intent intent = new Intent(this, GamesHistoryActivity.class);
        startActivity(intent);
        if (Settings.getAnimationsDisplay())
            overridePendingTransition(R.anim.zoom_decreasing_enter, R.anim.zoom_decreasing_exit);
    }

    private void switchToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        if (Settings.getAnimationsDisplay())
            overridePendingTransition(R.anim.zoom_increasing_enter, R.anim.zoom_increasing_exit);
    }
}