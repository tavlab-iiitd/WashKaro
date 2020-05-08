package inspire2connect.inspire2connect.satyaChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import inspire2connect.inspire2connect.R;

public class ChatAdapter extends BaseAdapter {

    ArrayList<ChatElem> messages;
    Context context;

    public ChatAdapter(Context context, ArrayList<ChatElem> messages){
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
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
        MyViewHolder holder;
        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msglist,parent,false);
            holder = new MyViewHolder(convertView);

            convertView.setTag(holder);

        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        if(messages.get(position).isMe){
            holder.rightText.setText(messages.get(position).text);
            holder.leftText.setVisibility(View.INVISIBLE);
            holder.rightText.setVisibility(View.VISIBLE);
        }
        else{
            holder.leftText.setText(messages.get(position).text);
            holder.rightText.setVisibility(View.INVISIBLE);
            holder.leftText.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public class MyViewHolder {
        TextView rightText,leftText;
        public MyViewHolder(View itemView) {
            rightText = itemView.findViewById(R.id.rightText);
            leftText = itemView.findViewById(R.id.leftText);
        }
    }
}
