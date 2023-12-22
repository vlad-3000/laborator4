package com.example.lab_4.service;

import android.content.Context;

import com.example.lab_4.SongAsyncTask;
import com.example.lab_4.service.impl.SQLiteMultimediaImpl;

import java.util.logging.Logger;

public class Service {
    public static final Long TIME_REPEAT = 5000L;
    private final Logger log = Logger.getLogger(Service.class.getSimpleName());
    private final static String RADIO_URI = "https://media.itmo.ru/includes/get_song.php";
    private static Service instance;
    public MultimediaRepository multimediaRepository;

    public static Service getInstance() {
        return instance;
    }

    public static Service createInstance(Context context) {
        if (instance == null) instance = new Service(context);
        return instance;
    }

    public Service(Context context) {
        MultimediaSQLiteHelper helper = new MultimediaSQLiteHelper(context);
        this.multimediaRepository = new SQLiteMultimediaImpl(helper);
    }

    public void startPolling(SongAsyncTask.ResultListener listener) {
        Runnable runnable = () -> {
            SongAsyncTask task = new SongAsyncTask(RADIO_URI, listener);
            task.execute();
            startPolling(listener);
            CommonService.getInstance().showToast("Обновление...");
        };
        CommonService.getInstance().getHandler().postDelayed(runnable, TIME_REPEAT);
    }
}
