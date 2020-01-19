package org.zafritech.zscode.administrator.views.fragments.workflow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkflowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WorkflowViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is Workflow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}