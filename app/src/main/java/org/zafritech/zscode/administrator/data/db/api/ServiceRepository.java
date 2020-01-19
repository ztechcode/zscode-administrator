package org.zafritech.zscode.administrator.data.db.api;

import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class ServiceRepository {

    private MutableLiveData<List<Service>> searchResults = new MutableLiveData<>();

    private void asyncFinished(List<Service> results) {

        searchResults.setValue(results);
    }

    private static class QueryAsyncTask extends AsyncTask<Integer, Void, Service> {

        private ServiceDao asyncTaskDao;
        private ServiceRepository delegate = null;

        QueryAsyncTask(ServiceDao dao) {

            asyncTaskDao = dao;
        }

        @Override
        protected Service doInBackground(final Integer... params) {

            return asyncTaskDao.findService(params[0]);
        }
    }
}
