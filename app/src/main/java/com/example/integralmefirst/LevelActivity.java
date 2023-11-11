package com.example.integralmefirst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {
    public static int lastStageNumber = 3;
    private int currentStageNumber = 1;
    private ArrayList<String> correctAnswers;
    private EmptyFieldsRecViewAdapter emptyFieldsRecViewAdapter;
    private AnswersRecViewAdapter answersRecViewAdapter;
    private TextView stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        int difficulty = getIntent().getIntExtra("difficulty", -1);
        TextView headline = findViewById(R.id.levelHeadline);
        String newHeadlineText = getString(R.string.difficulty) + difficulty;
        headline.setText(newHeadlineText);
        stage = findViewById(R.id.stage);
        updateStage();


        int operationsNumber = 5;  //todo import number of operations
        ArrayList<String> emptyFieldsValues = new ArrayList<>();
        for (int i = 0; i < operationsNumber; i++) {
            emptyFieldsValues.add("$\\,$");
        }
        emptyFieldsRecViewAdapter = new EmptyFieldsRecViewAdapter(this);
        emptyFieldsRecViewAdapter.setEmptyFields(emptyFieldsValues);
        RecyclerView emptyFieldsRecView = findViewById(R.id.EmptyFieldsRecyclerView);
        emptyFieldsRecView.setAdapter(emptyFieldsRecViewAdapter);
        emptyFieldsRecView.setLayoutManager(new LinearLayoutManager(this));

        int answersNumber = 7;  //todo import correct answers
        ArrayList<String> answersValues = new ArrayList<>();
        correctAnswers = new ArrayList<>();
        for (int i = 0; i < answersNumber; i++) {                    // adding random data
            String sampleAnswer = (i + 1) + " $x=\\frac{1+y}{1+2z^2}$";
            answersValues.add(sampleAnswer);
            if (i < operationsNumber)
                correctAnswers.add(sampleAnswer);
        }
        answersRecViewAdapter = new AnswersRecViewAdapter(this);
        answersRecViewAdapter.setAnswers(answersValues);
        RecyclerView answersRecView = findViewById(R.id.AnswersRecyclerView);
        answersRecView.setAdapter(answersRecViewAdapter);
        answersRecView.setLayoutManager(new GridLayoutManager(this, 2));

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areAnswersCorrect()) {
                    currentStageNumber++;
                    if (currentStageNumber > lastStageNumber) {
                        finish();
                    } else {
                        updateStage();
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

    public String setSelectedEmptyField(String data) {
        return emptyFieldsRecViewAdapter.setSelectedEmptyField(data);
    }

    private boolean areAnswersCorrect() {
        return emptyFieldsRecViewAdapter.checkIfFieldsMatchWith(correctAnswers);
    }

    private void wrongAnswersToast() {
        Toast toast = Toast.makeText(this, R.string.some_answers_are_incorrect,
                Toast.LENGTH_LONG);
        toast.show();
    }
}