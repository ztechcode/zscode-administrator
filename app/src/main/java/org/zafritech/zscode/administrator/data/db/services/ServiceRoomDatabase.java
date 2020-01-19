package org.zafritech.zscode.administrator.data.db.services;

import android.content.Context;

import org.zafritech.zscode.administrator.data.db.tasks.WordRoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Service.class}, version = 1, exportSchema = false)
public abstract class ServiceRoomDatabase extends RoomDatabase {

    public abstract ServiceDao serviceDao();

    private static volatile ServiceRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ServiceRoomDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (ServiceRoomDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ServiceRoomDatabase.class, "services_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }

            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {

            super.onOpen(db);
        }
    };
}
