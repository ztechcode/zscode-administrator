package org.zafritech.zscode.administrator.data.db.services;

import android.app.Application;
import android.os.AsyncTask;

public class ServicesInit {

    private Application application;

    public ServicesInit(Application application) {

        this.application = application;
    }

    public void init() {

        new InitTask().execute();
    }

    private class InitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ServiceRepository repository = new ServiceRepository(application);

            // Clear database first
            repository.deleteAll();

            Service discovery = new Service("Discovery", "https://services.zafritech.net/eureka");
            repository.insert(discovery);

            Service gateway = new Service("Gateway", "https://services.zafritech.net/gateway");
            repository.insert(gateway);

            Service auth = new Service("AuthHelper", "https://services.zafritech.net/auth");
            repository.insert(auth);

            Service monitoring = new Service("Monitoring", "https://services.zafritech.net/monitor");
            repository.insert(monitoring);

            Service accounts = new Service("Accounts", "https://services.zafritech.net/accounts");
            repository.insert(accounts);

            Service tasks = new Service("Tasks", "https://services.zafritech.net/todos/tasks");
            repository.insert(tasks);

            Service messages = new Service("Messages", "https://services.zafritech.net/messages");
            repository.insert(messages);

            Service workflow = new Service("Workflow", "https://services.zafritech.net/workflow");
            repository.insert(workflow);

            return null;
        }
    }
}
