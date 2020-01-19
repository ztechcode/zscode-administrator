package org.zafritech.zscode.administrator.data.db.api;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ServiceDao {

    @Insert
    void insertService(Service service);

    @Query("SELECT * FROM services WHERE id = :id")
    Service findService(int id);

    @Query("DELETE FROM services WHERE id = :id")
    void deleteService(String id);

    @Query("SELECT * FROM services")
    LiveData<List<Service>> getAllProducts();
}
