package com.example.integralmefirst.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.integralmefirst.R;
import com.example.integralmefirst.database.DBHelper;
import com.google.android.material.slider.Slider;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button resetProblemsButton = findViewById(R.id.ResetProblemsButton);
        resetProblemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper helper = DBHelper.getCurrentDBHelper();
                helper.reloadAnswersAndProblems();
                Toast toast = Toast.makeText(SettingsActivity.this, R.string.reset_successful, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        Button removeUserDataButton = findViewById(R.id.RemoveDataButton);
        removeUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAreYouSureDialog();
            }
        });
        Slider slider = findViewById(R.id.SetIntegralsNumberBar);
        slider.setValue(Settings.getStagesPerLevel());
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Settings.setStagesPerLevel((int) slider.getValue());
            }
        });
        SwitchCompat switchReturnOnClick = findViewById(R.id.MoveTextSwtich);
        switchReturnOnClick.setChecked(Settings.isReturnOnClick());
        switchReturnOnClick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.setReturnOnClick(isChecked);
            }
        });

        SwitchCompat switchChronological = findViewById(R.id.OrderGamesSwitch);
        switchChronological.setChecked(Settings.getFromNewestGamesHistory());
        switchChronological.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.setFromNewestGamesHistory(isChecked);
            }
        });
    }

    private void showAreYouSureDialog() {
        AreYouSureDialog dialog = new AreYouSureDialog(this);
        dialog.show(getSupportFragmentManager(), "AreYouSureDialog");
    }
}