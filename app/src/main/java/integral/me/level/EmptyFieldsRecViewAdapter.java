package integral.me.level;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import integral.me.R;
import integral.me.settings.Settings;

import java.util.ArrayList;
import java.util.Objects;

// adapter for the empty fields that are place to hold the answers chosen by user
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
        // highlight the checked field
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
                int currentPosition = holder.getAdapterPosition();

                // if the ReturnOnClick option is selected and the field is not empty - return its
                // content to the answers pool
                if (!LevelActivity.emptyMathField.equals(fields.get(currentPosition)) &&
                        Settings.getReturnOnClick()) {
                    levelActivity.addToAnswers(fields.get(currentPosition));
                    fields.set(currentPosition, LevelActivity.emptyMathField);
                }
                // change checked field if needed
                if (!button.isChecked()) {
                    int oldSelectedPosition = selectedPosition;
                    selectedPosition = currentPosition;
                    button.setChecked(true);
                    notifyItemChanged(oldSelectedPosition);
                    notifyItemChanged(selectedPosition);
                } else {
                    notifyItemChanged(currentPosition);
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

    // returns the content of the selected empty field
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
        // do not select next field if the current selected field is the last one
        if (newSelectedPosition == 0) {
            notifyItemChanged(getItemCount() - 1);
            return;
        }
        MathRadioButton button = getMathRadioButtonSafe(newSelectedPosition);
        // the button should not be null
        if (button == null) {
            notifyItemChanged(selectedPosition);
            return;
        }
        button.callOnClick(); // selects next field
    }

    private MathRadioButton getMathRadioButtonSafe(int position) {
        MathRadioButton button = null;
        int maxIter = 100000;
        while (button == null && maxIter > 0) {
            // the method below doesn't always scroll properly, so additional scrolling is needed
            recyclerView.scrollToPosition(position);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            // sometimes because of the incorrect position the manager is not available immediately
            while (manager == null && maxIter > 0) {
                recyclerView.scrollBy(0, 5);
                manager = recyclerView.getLayoutManager();
                maxIter--;
            }
            if (maxIter < 1)
                break;
            View view = manager.findViewByPosition(position);
            // same thing with the view
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
        return button; // will likely return the button
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
