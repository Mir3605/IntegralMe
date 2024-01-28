package com.example.integralmefirst.mainmenu;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
    private int tutorialStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DBHelper(this); // DBHelper should be initialized at the beginning of the program
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
            tutorialStep = 0;
            displayTutorial(tutorialStep);
        }
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (Settings.getDisplayTutorial())
                    Settings.setDisplayTutorial(false);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
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
        GuideView.Builder builder = new GuideView.Builder(this)
                .setTitleTextColor(Color.WHITE)
                .setContentTextColor(Color.WHITE)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        tutorialStep++;
                        displayTutorial(tutorialStep);
                    }
                });

        switch (step) {
            case 0: {
                builder.setPointerType(PointerType.none)
                        .setTitle(getString(R.string.tutorial))
                        .setContentText(getString(R.string.tutorial_description_0))
                        .setDismissType(DismissType.anywhere)
                        .setGravity(Gravity.center)
                        .setTargetView(findViewById(R.id.WelcomeText));
                break;
            }
            case 1: {
                builder.setTitle(getString(R.string.levels))
                        .setContentText(getString(R.string.tutorial_description_1))
                        .setDismissType(DismissType.anywhere)
                        .setGravity(Gravity.center)
                        .setTargetView(findViewById(R.id.ChooseLvlRecyclerView));
                break;
            }
            case 2: {
                builder.setTitle(getString(R.string.level))
                        .setContentText(getString(R.string.tutorial_description_2))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(levelsRecyclerView.getChildAt(0).findViewById(R.id.LvlButton));
                break;
            }
            case 21: {
                builder.setTitle(getString(R.string.congratulations))
                        .setContentText(getString(R.string.tutorial_description_21))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(findViewById(R.id.SettingsButton))
                        .setGravity(Gravity.center);
                break;
            }
            case 22: {
                Settings.setDisplayTutorial(false);
                return;
            }
            default:
                return;
        }
        GuideView guideView = builder.build();
        guideView.setBackgroundColor(0x11000000);
        guideView.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (tutorialStep == 3 && Settings.getDisplayTutorial()) {
            tutorialStep = 21;
            displayTutorial(tutorialStep);
        } else if (Settings.getDisplayTutorial()) {
            tutorialStep = 0;
            displayTutorial(tutorialStep);
        }
    }
}