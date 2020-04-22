package inspire2connect.inspire2connect.about;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import inspire2connect.inspire2connect.R;

public class about_adapter extends BaseAdapter {
    private final Context mContext;
    private final String[] names;
    private final String[] sals;

    public about_adapter(Context mContext,String[] names,String[] sals) {
        this.mContext = mContext;
        this.names = names;
        this.sals = sals;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String name = names[position];
        final String sal = sals[position];

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.about_grid_item, null);
        }

        // 3
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_abt_name);
        final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_author);

        // 4
        nameTextView.setText(name);
        authorTextView.setText(sal);

        return convertView;
    }
}
