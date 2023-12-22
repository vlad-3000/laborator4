package com.example.lab_4;

import android.app.Application;

import com.example.lab_4.service.CommonService;
import com.example.lab_4.service.Service;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeServices();
        if (!CommonService.checkInternet(getApplicationContext())) {
            CommonService.getInstance().showToast("Приложение запущено в автономном режиме! Доступен только просмотр записей!");
        }
    }

    private void initializeServices() {
        CommonService.createInstance(getApplicationContext());
        Service.createInstance(getApplicationContext());
    }
}
