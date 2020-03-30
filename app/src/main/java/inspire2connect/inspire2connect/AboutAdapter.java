package inspire2connect.inspire2connect;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<AboutModel> imageModelArrayList;

    public AboutAdapter(Context ctx, ArrayList<AboutModel> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public AboutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.about_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AboutAdapter.MyViewHolder holder, int position) {

        holder.iv.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        holder.name_tv.setText(imageModelArrayList.get(position).getName());
        holder.github_tv.setText(imageModelArrayList.get(position).getGithub());
        holder.linkedin_tv.setText(imageModelArrayList.get(position).getLinkedin());
        holder.twitter_tv.setText(imageModelArrayList.get(position).getTwitter());
        holder.email_tv.setText(imageModelArrayList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_tv, github_tv, linkedin_tv, twitter_tv, email_tv;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            github_tv = (TextView) itemView.findViewById(R.id.github_tv);
            linkedin_tv = (TextView) itemView.findViewById(R.id.linkedin_tv);
            twitter_tv = (TextView) itemView.findViewById(R.id.twitter_tv);
            email_tv = (TextView) itemView.findViewById(R.id.email_tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}