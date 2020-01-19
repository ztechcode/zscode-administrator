package org.zafritech.zscode.administrator.data.db.services;

import android.app.Application;

import org.zafritech.zscode.administrator.data.db.tasks.Word;
import org.zafritech.zscode.administrator.data.db.tasks.WordRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ServiceViewModel extends AndroidViewModel {

    private ServiceRepository mRepository;

    private LiveData<List<Service>> mAllServices;

    public ServiceViewModel(@NonNull Application application) {

        super(application);

        mRepository = new ServiceRepository(application);

        mAllServices = mRepository.getAllServices();
    }

    public LiveData<List<Service>> getAllServices() {

        return mAllServices;
    }

    public void insert(Service service) {

        mRepository.insert(service);
    }

    public void update(Service service) {

        mRepository.update(service);
    }
}
