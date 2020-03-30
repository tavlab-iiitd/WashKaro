package inspire2connect.inspire2connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import inspire2connect.inspire2connect.R;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        //return super.onSupportNavigateUp();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void onNextClick(View v)
    {
        final EditText nametext=(EditText)findViewById(R.id.nameText);
        Intent intent=new Intent(getApplicationContext(), QuestionsActivity.class);
        Intent prevIntent = getIntent();
        intent.putExtra("language", prevIntent.getStringExtra("language"));
        startActivity(intent);
    }
}
