package inspire2connect.inspire2connect.contactTracer.sqlDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;


@Entity(tableName = "interactions")
public class InteractionEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    public String UUID;

    @ColumnInfo(name = "state")
    public int state;

    @ColumnInfo(name = "time")
    @TypeConverters({TimeConverter.class})
    public Date timeStamp;

}
