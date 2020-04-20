package inspire2connect.inspire2connect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {

    ArrayList<String> questions;
    Context context;

    public QuestionsAdapter(Context context, ArrayList<String> questions){
        this.context = context;
        this.questions = questions;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msglist,parent,false);
       MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }



    @Override
    public void onBindViewHolder( QuestionsAdapter.MyViewHolder holder, int position) {

        if(questions.get(position).equalsIgnoreCase("yes") || questions.get(position).equalsIgnoreCase("no")){
            holder.rightText.setText(questions.get(position));
            holder.leftText.setVisibility(View.GONE);
        }
        else{
            holder.leftText.setText(questions.get(position));
            holder.rightText.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rightText,leftText;
        public MyViewHolder(View itemView) {
            super(itemView);
            rightText = (TextView) itemView.findViewById(R.id.rightText);

                leftText = (TextView)itemView.findViewById(R.id.leftText);
        }
    }



}
