package inspire2connect.inspire2connect;


//import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class about extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AboutModel> imageModelArrayList;
    private AboutAdapter adapter;

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        //return super.onSupportNavigateUp();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.lang_togg_butt) {
            Toast.makeText(about.this, "Language Changed", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.Survey) {
            Intent i = new Intent(about.this, Male_Female.class);
            startActivity(i);
        } else if (id == R.id.developers) {
            Intent i = new Intent(about.this, about.class);
            startActivity(i);
        } else if (id == R.id.privacy_policy) {
            Intent i = new Intent(about.this, privacy_policy.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        imageModelArrayList = eatFruits();
        adapter = new AboutAdapter(this, imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private ArrayList<AboutModel> eatFruits() {
        String tempo = getResources().getString(R.string.link_test);
        Log.d("link_test", tempo);
        /*
        int[] myImageList = new int[]{R.drawable.kanav_image, R.drawable.rohan_image,R.drawable.vaibhav_image};
        String[] NameList = new String[]{"Kanav Bhagat","Rohan Pandey" ,"Vaibhav Gautam"};
        String[] GithubList=new String[]{"Github\nhttps://github.com/kanavbhagat","Github\nhttps://github.com/rohanpandey","Github\nhttps://github.com/VAICR7BHAV"};
        String[] LinkedInList=new String[]{"LinkedIn\nhttp://linkedin.com/in/kanav-bhagat-133229130","LinkedIn\nhttps://www.linkedin.com/in/rohan-pandey-145170175/","LinkedIn\nhttps://www.linkedin.com/in/vaibhav-gautam-171775187/"};
        String[] TwitterList=new String[]{"Twitter\nhttps://mobile.twitter.com/BhagatKanav","Twitter\nhttps://twitter.com/therohanpandey1","Twitter\nhttps://twitter.com/VAIBHAVGAUTAM13"};
        String[] EmailList=new String[]{"Email\nkanav16046@iiitd.ac.in","Email\nrohan99pandey@gmail.com","Email\nvg459@snu.edu.in"};
        */
        int[] myImageList = new int[]{R.drawable.logo, R.drawable.tavsir, R.drawable.kanav_image, R.drawable.rohan_image, R.drawable.vaibhav_image, R.drawable.himanshu};
        String[] NameList = new String[]{"TAVLAB", "Dr. Tavpritesh Sethi(Mentor)", "Kanav Bhagat(Developer)", "Rohan Pandey(Developer)", "Vaibhav Gautam(Developer)", "Himanshu Sharma (Developer)"};
        String[] GithubList = new String[]{"Email\ntavlabiiitd@gmail.com", "LinkedIn\nhttps://www.linkedin.com/in/tavpritesh/", "Github\nhttps://github.com/kanavbhagat", "Github\nhttps://github.com/rohanpandey", "Github\nhttps://github.com/VAICR7BHAV", "Github\nhttps://github.com/hspandit"};
        String[] LinkedInList = new String[]{"Website\nhttps://tavlab.iiitd.edu.in/", "Twitter\nhttps://twitter.com/Tavpritesh", "LinkedIn\nhttp://linkedin.com/in/kanav-bhagat-133229130", "LinkedIn\nhttps://www.linkedin.com/in/rohan-pandey-145170175/", "LinkedIn\nhttps://www.linkedin.com/in/vaibhav-gautam-171775187/", "LinkedIn\nhttps://www.linkedin.com/in/himanshu-sharma2950/"};
        String[] TwitterList = new String[]{"Github\nhttps://github.com/tavlab-iiitd", "Email\ntavpriteshsethi@iiitd.ac.in", "Twitter\nhttps://mobile.twitter.com/BhagatKanav", "Twitter\nhttps://twitter.com/therohanpandey1", "Twitter\nhttps://twitter.com/VAIBHAVGAUTAM13", "Twitter\nhttps://twitter.com/himanshu9132"};
        String[] EmailList = new String[]{"", "", "Email\nkanav16046@iiitd.ac.in", "Email\nrohan99pandey@gmail.com", "Email\nvg459@snu.edu.in", "Email\n 0hspandit0@gmail.com"};


        ArrayList<AboutModel> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AboutModel fruitModel = new AboutModel();
            fruitModel.setName(NameList[i]);
            fruitModel.setImage_drawable(myImageList[i]);
            fruitModel.setEmail(EmailList[i]);
            fruitModel.setGithub(GithubList[i]);
            fruitModel.setLinkedin(LinkedInList[i]);
            fruitModel.setTwitter(TwitterList[i]);
            list.add(fruitModel);
        }
        return list;
    }

}