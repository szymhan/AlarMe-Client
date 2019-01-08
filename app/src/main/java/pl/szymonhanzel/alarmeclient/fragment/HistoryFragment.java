package pl.szymonhanzel.alarmeclient.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.component.DatabaseAdapter;
import pl.szymonhanzel.alarmeclient.component.DatabaseListAdapter;
import pl.szymonhanzel.alarmeclient.model.SQLAlarmModel;


public class HistoryFragment extends Fragment {

    private ListView listView;
    private List<SQLAlarmModel> showList;
    private DatabaseAdapter dbAdapter;
    private DatabaseListAdapter listAdapter;
    private Button clearButton;

    private View.OnClickListener clearButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.sql_delete_all_title);  // GPS not found
            builder.setMessage(R.string.sql_delete_all_text); // Want to enable?
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    clearHistory();
                    listAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //       System.exit(0);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showList = new ArrayList<>();
        dbAdapter = new DatabaseAdapter(getContext());
        getData();
        listAdapter = new DatabaseListAdapter(getContext(), showList);
        listView = view.findViewById(R.id.history_fragment_listview);
        listView.setAdapter(listAdapter);
        clearButton = view.findViewById(R.id.history_fragment_clear_button);
        clearButton.setOnClickListener(clearButtonListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
        listAdapter.notifyDataSetChanged();
    }

    private void getData() {
        showList.clear();
        dbAdapter.open();
        Cursor cursor = dbAdapter.getAll();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            showList.add(new SQLAlarmModel(cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_VEHICLE_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DatabaseAdapter.KEY_DATE))
            ));
            cursor.moveToNext();
        }
        dbAdapter.close();
        sortList();
    }

    private void sortList() {
        Collections.sort(showList, new Comparator<SQLAlarmModel>() {
            @Override
            public int compare(SQLAlarmModel o1, SQLAlarmModel o2) {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    private void clearHistory() {
        showList.clear();
        dbAdapter.open();
        dbAdapter.deleteAll();
        dbAdapter.close();

    }
}
