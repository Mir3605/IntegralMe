package com.example.integralmefirst.level;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integralmefirst.R;
import com.example.integralmefirst.gameshistory.GameCardRecViewAdapter;
import com.example.integralmefirst.gameshistory.GameData;

public class LevelSummaryDialog extends DialogFragment {
    private final GameData gameData;
    public LevelSummaryDialog(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        requireActivity().finish();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // init
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View builderView = inflater.inflate(R.layout.level_summary_dialog, null);

        // finish on touch
        View.OnTouchListener finishOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                requireActivity().finish();
                return true;
            }
        };
        builderView.findViewById(R.id.LevelSummaryRelativeLayout).setOnTouchListener(finishOnTouchListener);
        builderView.findViewById(R.id.cardInSummary).
                findViewById(R.id.OverlapOfGameCardRelativeLayout).
                setOnTouchListener(finishOnTouchListener);

        // inserting data
        TextView points = builderView.findViewById(R.id.PointsGameCard);
        String pointsText = points.getText().toString();
        pointsText += " " + gameData.getPoints();
        points.setText(pointsText);

        TextView date = builderView.findViewById(R.id.DateGameCard);
        date.setText(gameData.getDateAsString());

        RecyclerView recyclerView = builderView.findViewById(R.id.cardInSummary).findViewById(R.id.GameCardRecyclerView);
        GameCardRecViewAdapter adapter = new GameCardRecViewAdapter();
        adapter.setTimesAndProblems(gameData.getTimes(), gameData.getProblems());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // returning dialog
        builder.setView(builderView);
        return builder.create();
    }
}
