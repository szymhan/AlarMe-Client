package pl.szymonhanzel.alarmeclient.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.model.SQLAlarmModel;

public class DatabaseListAdapter extends ArrayAdapter<SQLAlarmModel> {

    public DatabaseListAdapter( Context context,  List<SQLAlarmModel> sqlAlarmModelList) {
        super(context,R.layout.single_row, sqlAlarmModelList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SQLAlarmModel alarmModel = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_row,parent,false);

        }
        ImageView icon = convertView.findViewById(R.id.item_list_image);
        TextView address = convertView.findViewById(R.id.item_list_address);
        TextView date = convertView.findViewById(R.id.item_list_date);

        icon.setImageResource(0);
        icon.setImageResource(imageResourceCode(alarmModel.getVehicleType()));
        address.setText(alarmModel.getAddress());
        date.setText(alarmModel.getDate());
        return convertView;

    }


    private int imageResourceCode (String name) {
        switch (name) {
            case "Straż pożarna":
                return R.drawable.firefighter;
            case "Policja" :
                return R.drawable.policeCar;
            case "Ambulans" :
                return R.drawable.ambulance;
        }
        return 0;
    }
}
