package com.example.integralmefirst.level;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;

import java.util.ArrayList;
import java.util.Collections;

import katex.hourglass.in.mathlib.MathView;

public class LevelActivity extends AppCompatActivity {
    public static final int seriesMultiplier = 20;
    private int lastStageNumber;
    private long startTime;
    private long[] timeList;
    private ArrayList<Integer> problemIds;
    private int currentStageNumber;
    private int difficulty;
    private int points;
    private int series;
    private ArrayList<String> correctAnswers;
    private EmptyFieldsRecViewAdapter emptyFieldsRecViewAdapter;
    private TextView stage;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_level);
        helper = DBHelper.getCurrentDBHelper();

        // getting stage, points and difficulty
        difficulty = getIntent().getIntExtra("difficulty", -1);
        problemIds = getIntent().getIntegerArrayListExtra("chosenProblems");
        timeList = getIntent().getLongArrayExtra("timeList");
        points = getIntent().getIntExtra("points", 0);
        series = getIntent().getIntExtra("series", 0);
        currentStageNumber = getIntent().getIntExtra("stageNum", 1);
        assert problemIds != null;
        lastStageNumber = problemIds.size();
        TextView pointsView = findViewById(R.id.points);
        pointsView.setText(String.valueOf(points));
        TextView headline = findViewById(R.id.levelHeadline);
        String newHeadlineText = getString(R.string.difficulty) + difficulty;
        headline.setText(newHeadlineText);
        stage = findViewById(R.id.stage);
        updateStage();
        int problemId = problemIds.get(currentStageNumber - 1);

        // inserting question
        MathView question = findViewById(R.id.Question);
        String questionValue = helper.getProblemValueById(problemId);
        question.setDisplayText(questionValue);

        initializeEmptyFieldsRecView(problemId);

        initializeAnswersRecView();

        setCheckButtonFunctionality();

        startTime = System.currentTimeMillis();
    }

    private void initializeEmptyFieldsRecView(int problemId) {
        correctAnswers = helper.getAnswers(problemId);
        int operationsNumber = correctAnswers.size();
        ArrayList<String> emptyFieldsValues = new ArrayList<>();
        for (int i = 0; i < operationsNumber; i++) {
            emptyFieldsValues.add("$\\,$");
        }
        emptyFieldsRecViewAdapter = new EmptyFieldsRecViewAdapter(this);
        emptyFieldsRecViewAdapter.setEmptyFields(emptyFieldsValues);
        RecyclerView emptyFieldsRecView = findViewById(R.id.EmptyFieldsRecyclerView);
        emptyFieldsRecView.setAdapter(emptyFieldsRecViewAdapter);
        emptyFieldsRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAnswersRecView() {
        ArrayList<String> answersValues = new ArrayList<>(correctAnswers);
        if (difficulty == 0)
            answersValues.addAll(helper.getRandomAnswers(difficulty, 4));
        else
            answersValues.addAll(helper.getRandomAnswers(difficulty, 2));
        Collections.shuffle(answersValues);
        AnswersRecViewAdapter answersRecViewAdapter = new AnswersRecViewAdapter(this);
        answersRecViewAdapter.setAnswers(answersValues);
        RecyclerView answersRecView = findViewById(R.id.AnswersRecyclerView);
        answersRecView.setAdapter(answersRecViewAdapter);
        answersRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCheckButtonFunctionality() {
        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answersCorrect()) {
                    correctAnswerToast();
                    points += (difficulty + 1) * 10 + seriesMultiplier * series;
                    series++;
                    timeList[currentStageNumber - 1] = System.currentTimeMillis() - startTime;
                    if (currentStageNumber >= lastStageNumber) {
                        helper.addGameToHistory(problemIds, timeList, points);
                        finish();
                    } else {
                        moveToNextStage();
                    }
                } else {
                    wrongAnswersToast();
                    series = 0;
                }
            }
        });
    }

    private void updateStage() {
        String newStageText = currentStageNumber + "/" + lastStageNumber;
        stage.setText(newStageText);
    }

    private void moveToNextStage() {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("chosenProblems", problemIds);
        intent.putExtra("points", points);
        intent.putExtra("series", series);
        intent.putExtra("stageNum", currentStageNumber + 1);
        intent.putExtra("timeList", timeList);
        startActivity(intent);
        finish();
    }

    public String setSelectedEmptyField(String data) {
        return emptyFieldsRecViewAdapter.setSelectedEmptyField(data);
    }

    private boolean answersCorrect() {
        return emptyFieldsRecViewAdapter.checkIfFieldsMatchWith(correctAnswers);
    }

    private void wrongAnswersToast() {
        Toast toast = Toast.makeText(this, R.string.some_answers_are_incorrect,
                Toast.LENGTH_LONG);
        toast.show();
    }

    private void correctAnswerToast() {
        Toast toast = Toast.makeText(this, getString(R.string.correct_answer_toast_text) +
                series * seriesMultiplier, Toast.LENGTH_SHORT);
        toast.show();
    }
}