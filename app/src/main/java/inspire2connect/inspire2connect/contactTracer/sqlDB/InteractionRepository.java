package inspire2connect.inspire2connect.contactTracer.sqlDB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import java.util.Date;
import java.util.List;

public class InteractionRepository {
    private static final String DB_NAME = "interactions";
    private InteractionDatabase interactionDatabase;
    public InteractionRepository(Context context) {
        interactionDatabase = Room.databaseBuilder(context, InteractionDatabase.class, DB_NAME).build();
    }

    public void insertTask(String uuid, Date timeStamp, int state) {
        InteractionEntity entity = new InteractionEntity();
        entity.UUID = uuid;
        entity.state = state;
        entity.timeStamp = timeStamp;
        insertTask(entity);
    }

    public void insertTask(final InteractionEntity entity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                interactionDatabase.interactionDao().insertTask(entity);
                return null;
            }
        }.execute();
    }

    public String getUUIDs() {
//        String res;
//        try {
//            res = new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... voids) {
//                    List<InteractionEntity> list = interactionDatabase.interactionDao().getAll();
//                    String res = "";
//                    for (int i=0; i<list.size(); i++) {
//                        res += list.get(i).UUID + "\n";
//                    }
//                    return null;
//                }
//            }.execute().get();
//        } catch (Exception e) {
//
//        }

        List<InteractionEntity> list = interactionDatabase.interactionDao().getAll();
        String res = list.size()+"";
       /* for (int i=0; i<list.size(); i++) {
            res += list.get(i).UUID + "\n";
        } */
        return res;
    }
}
