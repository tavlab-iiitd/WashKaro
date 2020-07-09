package inspire2connect.inspire2connect.satyaChat;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
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

    public ChatAdapter(Context context, ArrayList<ChatElem> messages) {
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
        if (convertView == null) {
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.msglist, parent, false);
            holder = new MyViewHolder(convertView);

            convertView.setTag(holder);

        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        if (messages.get(position).isMe) {
            holder.rightText.setText(messages.get(position).text.trim());
            holder.leftText.setVisibility(View.GONE);
            holder.rightText.setVisibility(View.VISIBLE);
        } else {

            if (!messages.get(position).isMe) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.leftText.setText(Html.fromHtml(messages.get(position).text, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    holder.leftText.setText(Html.fromHtml(messages.get(position).text));
                }
            }

            holder.rightText.setVisibility(View.GONE);
            holder.leftText.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public class MyViewHolder {
        TextView rightText, leftText;

        public MyViewHolder(View itemView) {
            rightText = itemView.findViewById(R.id.rightText);
            leftText = itemView.findViewById(R.id.leftText);
        }
    }
}
