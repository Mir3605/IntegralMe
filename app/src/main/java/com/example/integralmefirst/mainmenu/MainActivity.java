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
import com.example.integralmefirst.settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int difficultyLevelsNumber = 3;
    private static int stagesInLevel = 3;

    public static int getStagesInLevel() {
        return stagesInLevel;
    }

    public static void setStagesInLevel(int stagesInLevel) {
        MainActivity.stagesInLevel = stagesInLevel;
        DBHelper.getCurrentDBHelper().changeStagesNumberInDatabase(stagesInLevel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper helper = new DBHelper(this); // DBHelper should be initialized at the beginning of the program
        setStagesInLevel(helper.getStagesNumber());
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
    }

    private void switchToGamesHistory() {
        Intent intent = new Intent(this, GamesHistoryActivity.class);
        startActivity(intent);
    }

    private void switchToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}