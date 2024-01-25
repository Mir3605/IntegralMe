package com.example.integralmefirst.mainmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;
import com.example.integralmefirst.gameshistory.GamesHistoryActivity;
import com.example.integralmefirst.settings.Settings;
import com.example.integralmefirst.settings.SettingsActivity;

import java.util.ArrayList;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class MainActivity extends AppCompatActivity {
    public static final int difficultyLevelsNumber = 3;
    private RecyclerView levelsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper helper = new DBHelper(this); // DBHelper should be initialized at the beginning of the program
        Settings.readSettingsFromDB();

        levelsRecyclerView = findViewById(R.id.ChooseLvlRecyclerView);
        ArrayList<Lvl> lvlsArray = new ArrayList<>();
        for (int i = 0; i < difficultyLevelsNumber; i++) {
            lvlsArray.add(new Lvl(this, i));
        }
        LevelsRecViewAdapter adapter = new LevelsRecViewAdapter(this);
        adapter.setLvls(lvlsArray);
        levelsRecyclerView.setAdapter(adapter);
        levelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            displayTutorial(0);
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

    private void displayTutorial(int step) {
        GuideListener nextTutorialStep = new GuideListener() {
            @Override
            public void onDismiss(View view) {
                displayTutorial(step + 1);
            }
        };

        switch (step) {
            case 0: {
                new GuideView.Builder(this)
                        .setPointerType(PointerType.none)
                        .setTitle(getString(R.string.tutorial))
                        .setContentText(getString(R.string.tutorial_description_0))
                        .setTitleTextColor(Color.WHITE)
                        .setContentTextColor(Color.WHITE)
                        .setDismissType(DismissType.anywhere)
                        .setGuideListener(nextTutorialStep)
                        .setGravity(Gravity.center)
                        .setTargetView(findViewById(R.id.WelcomeText))
                        .build()
                        .show();
                break;
            }
            case 1: {
                new GuideView.Builder(this)
                        .setTitle(getString(R.string.levels))
                        .setContentText(getString(R.string.tutorial_description_1))
                        .setTitleTextColor(Color.WHITE)
                        .setContentTextColor(Color.WHITE)
                        .setDismissType(DismissType.anywhere)
                        .setGuideListener(nextTutorialStep)
                        .setGravity(Gravity.center)
                        .setTargetView(findViewById(R.id.ChooseLvlRecyclerView))
                        .build()
                        .show();
                break;
            }
            case 2: {
                new GuideView.Builder(this)
                        .setTitle(getString(R.string.level))
                        .setContentText("Choose level 0 to begin with")
                        .setTitleTextColor(Color.WHITE)
                        .setContentTextColor(Color.WHITE)
                        .setDismissType(DismissType.targetView)
                        .setGuideListener(nextTutorialStep)
                        .setTargetView(levelsRecyclerView.getChildAt(0))
                        .build()
                        .show();
                break;
            }
            case 3: {
                Button button = (Button) levelsRecyclerView.getChildAt(0).findViewById(R.id.LvlButton);
                button.performClick();
                break;
            }
        }
    }
}