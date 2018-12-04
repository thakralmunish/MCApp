package com.example.thakr.newspaper_testapp_1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class QuizService extends Service {
    public QuizService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
