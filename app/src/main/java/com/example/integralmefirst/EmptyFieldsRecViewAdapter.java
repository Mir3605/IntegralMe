package com.example.integralmefirst;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmptyFieldsRecViewAdapter extends RecyclerView.Adapter<EmptyFieldsRecViewAdapter.ViewHolder> {
    private ArrayList<String> fields = new ArrayList<>();
    private LevelActivity mainActivity;

    public EmptyFieldsRecViewAdapter(LevelActivity levelActivity) {
        this.mainActivity = levelActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_fields_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String field = fields.get(position);
        holder.emptyField.setText("          ");

    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public void setEmptyFields(ArrayList<String> emptyFieldsList){
        this.fields = emptyFieldsList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RadioButton emptyField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyField = itemView.findViewById(R.id.emptyField);
        }
    }
}
