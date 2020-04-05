package inspire2connect.inspire2connect.contactTracer.sqlDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {InteractionEntity.class}, version = 1, exportSchema = false)
public abstract class InteractionDatabase extends RoomDatabase {
    public abstract InteractionDao interactionDao();
}
