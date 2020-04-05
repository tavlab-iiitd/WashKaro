package inspire2connect.inspire2connect.contactTracer.sqlDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// Data = Data Access Objects
@Dao
public interface InteractionDao {

    @Query("SELECT * FROM interactions")
    List<InteractionEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(InteractionEntity interactionEntity);

    @Delete
    void delete(InteractionEntity interactionEntity);
}
