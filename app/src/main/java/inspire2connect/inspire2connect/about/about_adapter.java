package inspire2connect.inspire2connect.about;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import inspire2connect.inspire2connect.R;

public class about_adapter extends BaseAdapter {
    private final Context mContext;
//    private final String[] names;
    private final aboutElem[] names;

    public about_adapter(Context mContext,aboutElem[] names) {
        this.mContext = mContext;
        this.names = names;
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

        final String name = names[position].name;
        final String sal = names[position].tag;

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.about_grid_item, null);
        }

        // 3
        final TextView nameTextView = convertView.findViewById(R.id.textview_abt_name);
        final TextView authorTextView = convertView.findViewById(R.id.textview_author);

        // 4
        nameTextView.setText(name);
        authorTextView.setText(sal.replace(", ", "\n"));

        return convertView;
    }
}
