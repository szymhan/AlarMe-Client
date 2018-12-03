package pl.szymonhanzel.alarmeclient.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
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
        listView = view.findViewById(R.id.history_listview);
        listView.setAdapter(listAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
        listAdapter.notifyDataSetChanged();
    }

    private void getData() {
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
    }
}
