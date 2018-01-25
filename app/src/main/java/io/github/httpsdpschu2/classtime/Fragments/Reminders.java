package io.github.httpsdpschu2.classtime.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.Activities.MainActivity;
import io.github.httpsdpschu2.classtime.Adapters.ReminderAdapter;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;
import io.github.httpsdpschu2.classtime.models.Reminder;

import java.util.List;

/**
 * Created by Dean on 2/18/17.
 */
public final class Reminders extends Fragment {

    public interface ItemTouchHelperAdapter {
        void onItemDismiss(int position, Context context);
    }

    RecyclerView mRecycler;
    ReminderAdapter mAdapter;
    List<Reminder> mData;
    private static Reminders mInstance;

    public static Reminders getInstance() {
        if(mInstance == null) {
            mInstance = new Reminders();
        }
        return mInstance;
    }
    public Reminders() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.content_main, container,
                false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler);

        ReminderManager.getInstance().setContext(getContext());
        mData = ReminderManager.getInstance().getReminders();

        mRecycler.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(rootView.getContext());
        mRecycler.setLayoutManager(llm);

        mAdapter = new ReminderAdapter(mData, (MainActivity) getActivity());
        mRecycler.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecycler);

        return rootView;
    }
    public void clear() {
        mAdapter.onItemDismiss(0, getContext());
    }
    public void refresh() {
        if(getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mData = ReminderManager.getInstance().getReminders();
                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();
                }
            });
    }
    public ReminderAdapter getmAdapter(){
        return mAdapter;
    }
    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperAdapter mItemAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            mItemAdapter = adapter;
        }
        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());

            alertDialogBuilder.setTitle("Delete");
            alertDialogBuilder
                    .setMessage("Are you sure you want to delete this?")
                    .setCancelable(true)
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    })
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mItemAdapter.onItemDismiss(viewHolder.getAdapterPosition(), getContext());
                            ReminderManager.getInstance().getNextAlarm();
                        }
                    });

            // create alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button a = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            a.setTextColor(Color.parseColor("#0288D1"));
            b.setTextColor(Color.parseColor("#0288D1"));
        }
    }
}
