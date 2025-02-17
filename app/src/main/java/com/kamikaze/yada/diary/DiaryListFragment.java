package com.kamikaze.yada.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kamikaze.yada.R;

public class DiaryListFragment extends Fragment {
    public DiaryListFragment() {
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            DiaryHandler diaryHandler = new DiaryHandler(getContext());
            diaryHandler.loadData(recyclerView);
            DiaryListRecyclerViewAdapter adapter = new DiaryListRecyclerViewAdapter(context, diaryHandler.getDiaries());
            recyclerView.setAdapter(adapter);
            ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    View confirmDialog = LayoutInflater.from(context).inflate(R.layout.confirm_dialog, container, false);

                    new AlertDialog.Builder(context).setView(confirmDialog).setTitle("Are you sure you want to delete this diary?").setPositiveButton("Yes", (dialogInterface, i) -> {
                        diaryHandler.loadData();
                        diaryHandler.deleteDiary(viewHolder.getPosition(), recyclerView);
                        dialogInterface.cancel();
                    }).setNegativeButton("No", (dialogInterface, i) -> {
                        diaryHandler.loadData(recyclerView);
                        dialogInterface.cancel();
                    }).show();

                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView();
        DiaryHandler diaryHandler = new DiaryHandler(getContext());
        diaryHandler.loadData(recyclerView);
    }
}