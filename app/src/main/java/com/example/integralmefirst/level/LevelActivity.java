package com.example.integralmefirst.level;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private int lastStageNumber;
    private ArrayList<Integer> problemIds;
    private int currentStageNumber;
    private int difficulty;
    private ArrayList<String> correctAnswers;
    private EmptyFieldsRecViewAdapter emptyFieldsRecViewAdapter;
    private AnswersRecViewAdapter answersRecViewAdapter;
    private TextView stage;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        helper = DBHelper.getCurrentDBHelper();

        // getting stage and difficulty
        difficulty = getIntent().getIntExtra("difficulty", -1);
        problemIds = getIntent().getIntegerArrayListExtra("chosenProblems");
        assert problemIds != null;
        lastStageNumber = problemIds.size();
        currentStageNumber = getCurrentStageNumber();
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

        // inserting empty fields
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

        // inserting answers
        ArrayList<String> answersValues = new ArrayList<>(correctAnswers);
        if (difficulty == 0)
            answersValues.addAll(helper.getRandomAnswers(difficulty, 4));
        else
            answersValues.addAll(helper.getRandomAnswers(difficulty, 2));
        Collections.shuffle(answersValues);
        answersRecViewAdapter = new AnswersRecViewAdapter(this);
        answersRecViewAdapter.setAnswers(answersValues);
        RecyclerView answersRecView = findViewById(R.id.AnswersRecyclerView);
        answersRecView.setAdapter(answersRecViewAdapter);
        answersRecView.setLayoutManager(new GridLayoutManager(this, 2));

        // check button functionality
        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answersCorrect()) {
                    problemIds.set(currentStageNumber - 1, -1); // marking that current stage is finished
                    if (currentStageNumber >= lastStageNumber) {
                        finish();
                    } else {
                        moveToNextStage();
                    }
                } else {
                    wrongAnswersToast();
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

    private int getCurrentStageNumber() {
        for (int i = 0; i < lastStageNumber; i++) {
            if (problemIds.get(i) > -1)
                return i + 1;
        }
        throw new RuntimeException("All stages finished, cannot start new stage");
    }
}