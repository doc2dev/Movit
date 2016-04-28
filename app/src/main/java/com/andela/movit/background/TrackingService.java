/**
 * This class is a tracking service that will stay alive in the  background once it has been
 * started. It provides operations for receiving simple string commands using intents
 * and executing them. It also wraps a helper object that performs the actual tracking.
 * */

package com.andela.movit.background;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.andela.movit.config.Constants;

public class TrackingService extends Service {

    private volatile Looper looper;

    private volatile ServiceHandler serviceHandler;

    private ServiceHelper serviceHelper;

    private void handleCommand(Intent intent) {
        String command = intent.getStringExtra(Constants.COMMAND.getValue());
        if (command != null) {
            executeCommand(command);
        }
    }

    private void executeCommand(String command) {
        switch (command) {
            case "START":
                serviceHelper.startTracking();
                break;
            case "STOP":
                serviceHelper.stopTracking();
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("TrackingService");
        thread.start();
        looper = thread.getLooper();
        serviceHandler = new ServiceHandler(looper);
        serviceHelper = new ServiceHelper(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        serviceHandler.sendMessage(msg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        serviceHelper.stopTracking();
        looper.quit();
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            handleCommand((Intent)msg.obj);
        }
    }
}
