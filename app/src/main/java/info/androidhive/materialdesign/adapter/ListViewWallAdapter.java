package info.androidhive.materialdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.ListViewWall;

/**
 * Created by kha on 14/02/16.
 */
public class ListViewWallAdapter extends ArrayAdapter<ListViewWall> {

    public ListViewWallAdapter(Context context, List<ListViewWall> items) {
        super(context, R.layout.fragment_home, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_wall, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivDate = (TextView) convertView.findViewById(R.id.wallId);
            viewHolder.ivName = (TextView) convertView.findViewById(R.id.wallName);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        ListViewWall item = getItem(position);
        viewHolder.ivDate.setText(item.date);
        viewHolder.ivName.setText(item.name);

        return convertView;
    }

    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     *
     */
    private static class ViewHolder {
        TextView ivId;
        TextView ivName;
        TextView ivDate;
    }
}
