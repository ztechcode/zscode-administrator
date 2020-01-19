package org.zafritech.zscode.administrator.data.db.tasks;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class WordRepository {

    private WordDao mWordDao;

    private LiveData<List<Word>> mAllWords;

    WordRepository(Application application) {

        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);

        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    LiveData<List<Word>> getAllWords() {

        return mAllWords;
    }

    void insert(Word word) {

        WordRoomDatabase.databaseWriteExecutor.execute(() -> {

            mWordDao.insert(word);
        });
    }
}
