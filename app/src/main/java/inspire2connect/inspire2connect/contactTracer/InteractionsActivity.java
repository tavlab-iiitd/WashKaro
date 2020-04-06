//package inspire2connect.inspire2connect.contactTracer;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import inspire2connect.inspire2connect.R;
//import inspire2connect.inspire2connect.contactTracer.base.BaseActivity;
//import inspire2connect.inspire2connect.contactTracer.sqlDB.InteractionRepository;
//
//
//public class InteractionsActivity extends BaseActivity {
//
//    private TextView devices;
//
//    private InteractionRepository dbRepo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ca_activity_interactions);
//
//        dbRepo = new InteractionRepository(this.getApplicationContext());
//
//        this.devices = findViewById(R.id.interaction_devices);
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                final String res = dbRepo.getUUIDs();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        devices.setText(res);
//                    }
//                });
////                interactionDatabase.interactionDao().insertTask(entity);
//                return null;
//            }
//        }.execute();
//
//    }
//}
