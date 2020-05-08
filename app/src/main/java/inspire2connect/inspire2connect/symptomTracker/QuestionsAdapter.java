package inspire2connect.inspire2connect.symptomTracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import inspire2connect.inspire2connect.R;

public class QuestionsAdapter extends BaseAdapter {

    ArrayList<String> questions;
    Context context;

    public QuestionsAdapter(Context context, ArrayList<String> questions){
        this.context = context;
        this.questions = questions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msglist,parent,false);
            holder = new MyViewHolder(convertView);

            convertView.setTag(holder);

        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        if(questions.get(position).equals(context.getString(R.string.yes)) || questions.get(position).equals(context.getString(R.string.no))){
            holder.rightText.setText(questions.get(position));
            holder.leftText.setVisibility(View.INVISIBLE);
            holder.rightText.setVisibility(View.VISIBLE);
        }
        else{
            holder.leftText.setText(questions.get(position));
            holder.rightText.setVisibility(View.INVISIBLE);
            holder.leftText.setVisibility(View.VISIBLE);
        }

        return convertView;
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
    public int getCount() {
        return questions.size();
    }

    public class MyViewHolder {
        TextView rightText,leftText;
        public MyViewHolder(View itemView) {
            rightText = itemView.findViewById(R.id.rightText);
            leftText = itemView.findViewById(R.id.leftText);
        }
    }



}
