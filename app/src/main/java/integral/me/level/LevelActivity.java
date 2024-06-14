package integral.me.level;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import integral.me.R;
import integral.me.database.DBHelper;
import integral.me.gameshistory.GameData;
import integral.me.settings.Settings;

import java.util.ArrayList;
import java.util.Collections;

import katex.hourglass.in.mathlib.MathView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

// is called each time the new stage is reached
public class LevelActivity extends AppCompatActivity {
    public static final int seriesMultiplier = 20; // arbitrary chosen value
    public static final String emptyMathField = "$\\,$"; // displays nothing in latex
    private int lastStageNumber; // in other words: number of stages
    private long startTime;
    private long[] timeList;
    private ArrayList<Integer> problemIds;
    private int currentStageNumber;
    private int difficulty;
    private int points;
    private int series;
    private ArrayList<String> correctAnswers;
    private EmptyFieldsRecViewAdapter emptyFieldsRecViewAdapter;
    private AnswersRecViewAdapter answersRecViewAdapter;
    private RecyclerView emptyFieldsRecyclerView;
    private RecyclerView answersRecyclerView;
    private Button checkButton;
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

        initializeAnswersRecView(problemId);

        setCheckButtonFunctionality();

        startTime = System.currentTimeMillis();

        if (Settings.getDisplayTutorial()) {
            if (currentStageNumber == 1)
                displayTutorial(3);
            else if (currentStageNumber == 2)
                displayTutorial(11);
            else
                displayTutorial(16);
        }

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                QuitLevelDialog quitLevelDialog = new QuitLevelDialog();
                quitLevelDialog.show(getSupportFragmentManager(), "QUIT_DIALOG");
            }
        };
        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
    }

    private void initializeEmptyFieldsRecView(int problemId) {
        correctAnswers = helper.getAnswers(problemId);
        int operationsNumber = correctAnswers.size();
        ArrayList<String> emptyFieldsValues = new ArrayList<>();
        for (int i = 0; i < operationsNumber; i++) {
            emptyFieldsValues.add(emptyMathField);
        }
        emptyFieldsRecViewAdapter = new EmptyFieldsRecViewAdapter(this);
        emptyFieldsRecViewAdapter.setEmptyFields(emptyFieldsValues);
        emptyFieldsRecyclerView = findViewById(R.id.EmptyFieldsRecyclerView);
        emptyFieldsRecyclerView.setAdapter(emptyFieldsRecViewAdapter);
        emptyFieldsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAnswersRecView(int problemId) {
        ArrayList<String> answersValues = new ArrayList<>(correctAnswers);
        if (difficulty == 0)
            answersValues.addAll(helper.getRandomAnswersExcludingCorrect(difficulty, 4, problemId));
        else
            answersValues.addAll(helper.getRandomAnswersExcludingCorrect(difficulty, 2, problemId));
        if (!Settings.getDisplayTutorial())   // in the tutorial the correct answer should always be on top
            Collections.shuffle(answersValues);
        answersRecViewAdapter = new AnswersRecViewAdapter(this);
        answersRecViewAdapter.setAnswers(answersValues);
        answersRecyclerView = findViewById(R.id.AnswersRecyclerView);
        answersRecyclerView.setAdapter(answersRecViewAdapter);
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCheckButtonFunctionality() {
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answersCorrect()) {
                    correctAnswerToast();
                    points += (difficulty + 1) * 10 + seriesMultiplier * series;
                    series++;
                    timeList[currentStageNumber - 1] = System.currentTimeMillis() - startTime;
                    if (isLastLevel()) {
                        finishLevel();
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

    public boolean isLastLevel() {
        return currentStageNumber >= lastStageNumber;
    }

    private void updateStage() {
        String newStageText = currentStageNumber + "/" + lastStageNumber;
        stage.setText(newStageText);
    }

    private void finishLevel() {
        // add game data to the games history
        helper.addGameToHistory(problemIds, timeList, points);
        // retrieve this data in the nice format
        GameData gameData = helper.getNewestGameStats();
        // show level summary dialog
        LevelSummaryDialog dialog = new LevelSummaryDialog(gameData);
        dialog.show(getSupportFragmentManager(), "LevelSummaryDialog");
        getSupportFragmentManager().executePendingTransactions();
        Dialog dialog1 = dialog.getDialog();
        // apply "quit upon touch" functionality
        if (dialog1 != null) {
            Window window = dialog1.getWindow();
            if (window != null) {
                window.getDecorView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        finish();
                        return true;
                    }
                });
                Log.i("LevelActivity", "Successfully overrode onTouch");
            } else
                Log.i("LevelActivity", "dialog.getDialog().getWindow() produced null");
        } else
            Log.i("LevelActivity", "dialog.getDialog() produced null");
        // display tutorial message if needed
        if (Settings.getDisplayTutorial()) {
            GuideView guideView = new GuideView.Builder(this)
                    .setTargetView(dialog1.findViewById(R.id.LevelSummaryRelativeLayout))
                    .setTitleTextColor(Color.WHITE)
                    .setContentTextColor(Color.WHITE)
                    .setTitle("Level summary")
                    .setContentText("On the end of each level\na short summary is displayed.\nYou can view it again\nin the games history")
                    .setDismissType(DismissType.anywhere)
                    .setGuideListener(new GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            finish();
                        }
                    })
                    .build();
            guideView.setBackgroundColor(Color.TRANSPARENT);
            guideView.show();
        }
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

    @Override
    public void finish() {
        super.finish();
        // apply animations if needed
        if (!Settings.getAnimationsDisplay())
            return;
        if (isLastLevel())
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        else
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    void addToAnswers(String answer) {
        answersRecViewAdapter.addAnswer(answer);
    }

    private void displayTutorial(int step) {
        GuideView.Builder builder = new GuideView.Builder(this)
                .setTitleTextColor(Color.WHITE)
                .setContentTextColor(Color.WHITE)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        displayTutorial(step + 1);
                    }
                });

        switch (step) {
            case 3: {
                builder.setTitle(getString(R.string.stage_number))
                        .setContentText(getString(R.string.tutorial_description_3))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(stage);
                break;
            }
            case 4: {
                builder.setTitle(getString(R.string.points))
                        .setContentText(getString(R.string.tutorial_description_4))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(findViewById(R.id.points));
                break;
            }
            case 5: {
                builder.setTitle(getString(R.string.question))
                        .setContentText(getString(R.string.tutorial_description_5))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(findViewById(R.id.Question));
                break;
            }
            case 6: {
                builder.setTitle(getString(R.string.empty_fields))
                        .setContentText(getString(R.string.tutorial_description_6))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(findViewById(R.id.EmptyFieldsRecyclerView));
                break;
            }
            case 7: {
                builder.setTitle(getString(R.string.answers))
                        .setContentText(getString(R.string.tutorial_description_7))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(findViewById(R.id.AnswersRecyclerView));
                break;
            }
            case 8: {
                builder.setTitle(getString(R.string.answer_selection))
                        .setContentText(getString(R.string.tutorial_description_8))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(answersRecyclerView.getChildAt(0));
                break;
            }
            case 9: {
                builder.setTitle(getString(R.string.answer_inserted))
                        .setContentText(getString(R.string.tutorial_description_9))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(emptyFieldsRecyclerView.getChildAt(0));
                break;
            }
            case 10: {
                builder.setTitle(getString(R.string.continue_string))
                        .setContentText(getString(R.string.tutorial_description_10))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(checkButton);
                break;
            }
            case 11: {
                builder.setTitle(getString(R.string.points_update))
                        .setContentText(getString(R.string.tutorial_description_11))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(findViewById(R.id.points));
                break;
            }
            case 12: {
                builder.setTitle(getString(R.string.miss_click))
                        .setContentText(getString(R.string.tutorial_description_12))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(answersRecyclerView.getChildAt(1));
                break;
            }
            case 13: {
                builder.setTitle(getString(R.string.miss_click))
                        .setContentText(getString(R.string.tutorial_description_13))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(answersRecyclerView.getChildAt(0));
                break;
            }
            case 14: {
                builder.setTitle(getString(R.string.miss_click))
                        .setContentText(getString(R.string.tutorial_description_14))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(answersRecyclerView);
                break;
            }
            case 15: {
                builder.setTitle(getString(R.string.continue_string))
                        .setContentText(getString(R.string.tutorial_description_15))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(checkButton);
                break;
            }
            case 16: {
                builder.setTitle(getString(R.string.last_stage))
                        .setContentText(getString(R.string.tutorial_description_16))
                        .setDismissType(DismissType.anywhere)
                        .setTargetView(stage);
                break;
            }
            case 17: {
                builder.setTitle(getString(R.string.wrong_check))
                        .setContentText(getString(R.string.tutorial_description_17))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(answersRecyclerView.getChildAt(1));
                break;
            }
            case 18: {
                builder.setTitle(getString(R.string.wrong_check))
                        .setContentText(getString(R.string.tutorial_description_18))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(checkButton);
                break;
            }
            case 19: {
                builder.setTitle(getString(R.string.wrong_check))
                        .setContentText(getString(R.string.tutorial_description_19))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(answersRecyclerView.getChildAt(0));
                break;
            }
            case 20: {
                builder.setTitle(getString(R.string.finish))
                        .setContentText(getString(R.string.tutorial_description_20))
                        .setDismissType(DismissType.targetView)
                        .setTargetView(checkButton);
                break;
            }
            default:
                return;
        }
        GuideView guideView = builder.build();
        guideView.setBackgroundColor(0x11000000);
        guideView.show();
    }
}