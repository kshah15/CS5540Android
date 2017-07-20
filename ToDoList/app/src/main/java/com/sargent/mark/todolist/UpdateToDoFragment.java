package com.sargent.mark.todolist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

/**
 * Created by mark on 7/5/17.
 */

public class UpdateToDoFragment extends DialogFragment {

    private EditText toDo;
    private Spinner cat;    // spinner for selecting todoitem category
    private DatePicker dp;
    private Button add;
    private final String TAG = "updatetodofragment";
    private long id;


    public UpdateToDoFragment(){}

    // Add category to params
    public static UpdateToDoFragment newInstance(int year, int month, int day, String description, long id, String category) {
        UpdateToDoFragment f = new UpdateToDoFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putLong("id", id);
        args.putString("description", description);
        args.putString("category", category);   // Add category to args

        f.setArguments(args);

        return f;
    }

    //To have a way for the activity to get the data from the dialog
    public interface OnUpdateDialogCloseListener {
        // Add 'category' string to closeUpdateDialog method
        void closeUpdateDialog(int year, int month, int day, String description, long id, String category);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_adder, container, false);
        toDo = (EditText) view.findViewById(R.id.toDo);
        cat = (Spinner) view.findViewById(R.id.SpiCat);   // Get spinner by id
        dp = (DatePicker) view.findViewById(R.id.datePic);
        add = (Button) view.findViewById(R.id.add);

        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");
        id = getArguments().getLong("id");
        String description = getArguments().getString("description");
        dp.updateDate(year, month, day);

        toDo.setText(description);

        /*
            ##### Spinner setup
         */
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cat.setAdapter(adapter);
        // Set spinner selection
        selectSpinnerValue(cat, getArguments().getString("category"));

        add.setText("Update");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateToDoFragment.OnUpdateDialogCloseListener activity = (UpdateToDoFragment.OnUpdateDialogCloseListener) getActivity();
                Log.d(TAG, "id: " + id);
                // Add selected category item from spinner to closeDialog call
                activity.closeUpdateDialog(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), toDo.getText().toString(), id, cat.getSelectedItem().toString());
                UpdateToDoFragment.this.dismiss();
            }
        });

        return view;
    }

    // ##### Helper method to select an item of spinner by value, not position.
    // Code credited to: https://stackoverflow.com/questions/27122120/how-to-set-selected-item-of-spinner-by-value-not-by-position
    private void selectSpinnerValue(Spinner spinner, String myString) {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}