package inspire2connect.inspire2connect.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import inspire2connect.inspire2connect.R;

public class aboutAdapter extends BaseAdapter {

    Context context;
    aboutElem[] elems;
    LayoutInflater inflater;
    public aboutAdapter(Context context, aboutElem[] elems) {
            this.context = context;
            this.elems = elems;
            inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return elems.length;
    }

    @Override
    public Object getItem(int position) {
        return elems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.about_grid_item, null);
        TextView name = convertView.findViewById(R.id.aboutName);
        TextView tag = convertView.findViewById(R.id.aboutTag);
        name.setText(elems[position].name);
        tag.setText(elems[position].tag);
        return convertView;
    }
}
