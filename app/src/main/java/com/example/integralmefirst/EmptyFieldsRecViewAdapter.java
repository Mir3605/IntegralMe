package com.example.integralmefirst;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class EmptyFieldsRecViewAdapter extends RecyclerView.Adapter<EmptyFieldsRecViewAdapter.ViewHolder> {
    private ArrayList<String> fields = new ArrayList<>();
    private final LevelActivity levelActivity;
    private int selectedPosition = 0;
    private RecyclerView recyclerView;

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
        holder.emptyField.setChecked(position == selectedPosition);
        holder.emptyField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MathRadioButton button = (MathRadioButton) view;
                if (!button.isChecked()){
                    selectedPosition = holder.getAdapterPosition();
                    button.setChecked(true);
                    notifyDataSetChanged();
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

    public String setSelectedEmptyField(String data){
        String s = fields.get(selectedPosition);
        fields.set(selectedPosition, data);
        selectNextEmptyField();
        notifyDataSetChanged();
        return s;
    }
    public int getSelectedPosition(){
        return selectedPosition;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private MathRadioButton emptyField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyField = itemView.findViewById(R.id.emptyField);
        }
    }
    private void selectNextEmptyField(){
        int newSelectedPosition = (selectedPosition + 1)%getItemCount();
        MathRadioButton button = (MathRadioButton) Objects.requireNonNull(recyclerView.getLayoutManager()).
                findViewByPosition(newSelectedPosition);
        button.callOnClick();
    }
}
