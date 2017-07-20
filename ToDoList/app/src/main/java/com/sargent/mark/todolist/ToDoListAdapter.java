package com.sargent.mark.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sargent.mark.todolist.data.Contract;
import com.sargent.mark.todolist.data.DBHelper;
import com.sargent.mark.todolist.data.ToDoItem;

import java.util.ArrayList;

/**
 * Created by mark on 7/4/17.
 */

// Update to include 'category' and 'status'

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private String TAG = "todolistadapter";

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    //  Edit parameters to involved 'category' and 'status'
    public interface ItemClickListener {
        void onItemClick(int pos, String description, String duedate, long id, String category, String status);
    }

    public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            //RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView due;
        TextView descr;
        TextView cat;
        CheckBox stat;

        String duedate;
        String description;
        String category;
        String status;

        long id;

        ItemHolder(View view) {
            super(view);
            descr = (TextView) view.findViewById(R.id.description);
            due = (TextView) view.findViewById(R.id.dueDate);
            cat = (TextView)  view.findViewById(R.id.TvCategory);
            stat = (CheckBox) view.findViewById(R.id.ChStatus);
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
            Log.d(TAG, "deleting id: " + id);

            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
            category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));   // get category from db
            status = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_STATUS));   // get status from db

            due.setText(duedate);
            descr.setText(description);
            cat.setText(category);  // set category text

            // If status field is string 'Complete' then set checkbox as state to 'checked'.
            // TODO: refactor this into something less ugly..
            try {
                if (status.equals("Complete")) {
                    stat.setChecked(true);
                } else {
                    stat.setChecked(false);
                }
            } catch (NullPointerException e) {
                stat.setChecked(false);
            }

            // Set click listener for checkbox
            stat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Use static method MainActivity.updateTodoStatus for todoitem checkbox status
                    DBHelper helper = new DBHelper(v.getContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    MainActivity.updateTodoStatus(db, id, stat.isChecked());
                }
            });

            holder.itemView.setTag(id);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            // Edit to onItemClick to include 'category' and 'status'
            listener.onItemClick(pos, description, duedate, id, category, status);
        }
    }

}