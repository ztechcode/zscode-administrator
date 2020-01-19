package org.zafritech.zscode.administrator.data.db.services;

import org.zafritech.zscode.administrator.data.db.tasks.Word;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Service service);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Service service);

    @Query("DELETE FROM services_table")
    void deleteAll();

    @Query("SELECT * FROM services_table ORDER BY name ASC")
    List<Service> getAlphabetizedServicesList();

    @Query("SELECT * FROM services_table ORDER BY name ASC")
    LiveData<List<Service>> getAlphabetizedServices();

    @Query("SELECT * FROM services_table WHERE id = :id")
    Service getServiceById(Integer id);

}
