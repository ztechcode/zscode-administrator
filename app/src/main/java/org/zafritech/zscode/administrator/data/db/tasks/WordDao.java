package org.zafritech.zscode.administrator.data.db.tasks;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Query("DELETE FROM words_table")
    void deleteAll();

    @Query("SELECT * FROM words_table ORDER BY word ASC")
    LiveData<List<Word>> getAlphabetizedWords();
}
