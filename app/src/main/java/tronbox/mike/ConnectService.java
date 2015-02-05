package tronbox.mike;

import android.app.Service;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ConnectService extends Service implements CommonMetaData{

    public String TAG = "BluetoothLogs";
    private Record record;
    private boolean once = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.w(TAG,"Service has started");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Start_Record");
        intentFilter.addAction("Stop_Record");



        registerReceiver(broadcastReceiver, intentFilter);

        if(intent.getExtras() != null && intent.getExtras().containsKey("Action")){

            String action = intent.getExtras().getString("Action");

            if(action.equals("Speaker")){

                new Play().execute();

            }

        }

        record = new Record();



        return Service.START_STICKY;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String s = intent.getAction();

            if(s.equals("Start_Record")){

                  if(once == false)
                {
                    record.execute();
                    once = true;
                }else {

                      record.setCheck(true);
                  }


            }else if(s.equals("Stop_Record")){

                    record.setCheck(false);

            }
        }
    };

}
