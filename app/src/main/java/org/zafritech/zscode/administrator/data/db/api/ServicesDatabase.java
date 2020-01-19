package org.zafritech.zscode.administrator.data.db.api;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Service.class, exportSchema = false, version = 1)
public abstract class ServicesDatabase extends RoomDatabase {

    private static ServicesDatabase INSTANCE;

    public static synchronized ServicesDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ServicesDatabase.class, "services_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }

        return INSTANCE;
    }

    public abstract ServiceDao serviceDao();
}
