package com.example.integralmefirst.level;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.integralmefirst.R;
import com.example.integralmefirst.settings.Settings;

public class QuitLevelDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView message = new TextView(requireContext());
        message.setText(R.string.quit_level);
        int padding = 40;
        message.setPadding(padding, padding, padding, padding);
        message.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        builder.setCustomTitle(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Settings.getDisplayTutorial()) {
                            Settings.setDisplayTutorial(false);
                        }
                        requireActivity().finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
