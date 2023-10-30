package com.example.integralmefirst;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import androidx.appcompat.content.res.AppCompatResources;

import katex.hourglass.in.mathlib.MathView;

public class MathRadioButton extends MathView implements Checkable {
    private boolean checked;
    Context context;

    public MathRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setChecked(false);
    }

    @Override
    public void setChecked(boolean state) {

        checked = state;
        if (checked)
            setBackground(AppCompatResources.getDrawable(context, R.drawable.border_button_selected));
        else
            setBackground(AppCompatResources.getDrawable(context, R.drawable.border_button_unselected));
        invalidate();
        requestLayout();
    }

    public void setText(String text) {
        setDisplayText(text);
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

}
