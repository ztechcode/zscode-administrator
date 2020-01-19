package org.zafritech.zscode.administrator.data.db.tasks;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;

    private LiveData<List<Word>> mAllWords;

    public WordViewModel(@NonNull Application application) {

        super(application);

        mRepository = new WordRepository(application);

        mAllWords = mRepository.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {

        return mAllWords;
    }

    public void insert(Word word) {

        mRepository.insert(word);
    }
}
