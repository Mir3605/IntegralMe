package com.example.integralmefirst.level;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integralmefirst.R;

import java.util.ArrayList;
import java.util.Objects;

public class EmptyFieldsRecViewAdapter extends RecyclerView.Adapter<EmptyFieldsRecViewAdapter.ViewHolder> {
    private ArrayList<String> fields = new ArrayList<>();
    private final LevelActivity levelActivity;
    private int selectedPosition = 0;
    private final RecyclerView recyclerView;

    public EmptyFieldsRecViewAdapter(LevelActivity levelActivity) {
        this.levelActivity = levelActivity;
        recyclerView = levelActivity.findViewById(R.id.EmptyFieldsRecyclerView);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_field_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String field = fields.get(position);
        holder.emptyField.setText(field);
        if (position == selectedPosition) {
            holder.emptyField.setChecked(true);
            holder.emptyFieldBox.setBackground(AppCompatResources.getDrawable(levelActivity, R.drawable.border_button_selected));
        } else {
            holder.emptyField.setChecked(false);
            holder.emptyFieldBox.setBackground(AppCompatResources.getDrawable(levelActivity, R.drawable.border_button_unselected));
        }
        holder.emptyField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MathRadioButton button = (MathRadioButton) view;
                if (!button.isChecked()) {
                    int oldSelectedPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    button.setChecked(true);
                    notifyItemChanged(oldSelectedPosition);
                    notifyItemChanged(selectedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public void setEmptyFields(ArrayList<String> emptyFieldsList) {
        this.fields = emptyFieldsList;
        notifyDataSetChanged();
    }

    public String setSelectedEmptyField(String data) {
        String s = fields.get(selectedPosition);
        fields.set(selectedPosition, data);
        selectNextEmptyField();
        return s;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MathRadioButton emptyField;
        private RelativeLayout emptyFieldBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyField = itemView.findViewById(R.id.emptyField);
            emptyFieldBox = itemView.findViewById(R.id.emptyFieldBox);
        }
    }

    private void selectNextEmptyField() {
        int newSelectedPosition = (selectedPosition + 1) % getItemCount();
        if (newSelectedPosition == 0) {
            notifyItemChanged(getItemCount() - 1);
            return;
        }
        MathRadioButton button = getMathRadioButtonSafe(newSelectedPosition);
        if (button == null) {
            notifyItemChanged(selectedPosition);
            return;
        }
        button.callOnClick();
    }

    private MathRadioButton getMathRadioButtonSafe(int position) {
        MathRadioButton button = null;
        int maxIter = 100000;
        while (button == null && maxIter > 0) {
            recyclerView.scrollToPosition(position);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            while (manager == null && maxIter > 0) {
                recyclerView.scrollBy(0, 5);
                manager = recyclerView.getLayoutManager();
                maxIter--;
            }
            if (maxIter < 1)
                break;
            View view = manager.findViewByPosition(position);
            while (view == null && maxIter > 0) {
                recyclerView.scrollBy(0, 5);
                view = manager.findViewByPosition(position);
                maxIter--;
            }
            if (maxIter < 1)
                break;
            button = view.findViewById(R.id.emptyField);
            maxIter--;
        }
        return button;
    }

    public boolean checkIfFieldsMatchWith(@NonNull ArrayList<String> array) {
        if (array.size() != fields.size()) {
            return false;
        }
        for (int i = 0; i < array.size(); i++) {
            if (!Objects.equals(array.get(i), fields.get(i)))
                return false;
        }
        return true;
    }
}
