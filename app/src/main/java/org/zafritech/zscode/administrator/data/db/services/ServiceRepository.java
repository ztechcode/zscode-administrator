package org.zafritech.zscode.administrator.data.db.services;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ServiceRepository {

    private ServiceDao mServiceDao;
    private List<Service> mAllServicesList;
    private LiveData<List<Service>> mAllServices;

    public ServiceRepository(Application application) {

        ServiceRoomDatabase db = ServiceRoomDatabase.getDatabase(application);

        mServiceDao = db.serviceDao();
        mAllServicesList = mServiceDao.getAlphabetizedServicesList();
        mAllServices = mServiceDao.getAlphabetizedServices();
    }

    public List<Service> getAllServicesList() {

        return mAllServicesList;
    }

    public LiveData<List<Service>> getAllServices() {

        return mAllServices;
    }

    public Service insert(Service service) {

        ServiceRoomDatabase.databaseWriteExecutor.execute(() -> {

            mServiceDao.insert(service);
        });

        return service;
    }

    public void update(Service service) {

        ServiceRoomDatabase.databaseWriteExecutor.execute(() -> {

            mServiceDao.update(service);
        });
    }

    public void deleteAll() {

        mServiceDao.deleteAll();
    }
}
