package tronbox.mike;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class MyActivity extends FragmentActivity implements CommonMetaData{

    private String TAG = "MyActivity";
    private int START_BLUETOOTH = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new Friends()).commit();
                connectBluetooth();

            }
        }, 4000);


    }


      public void connectBluetooth()
    {
        Master.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!Master.bluetoothAdapter.isEnabled())
       {
           Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           startActivityForResult(intent, START_BLUETOOTH);

       }  else
        {

            showFriends();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){

             if(requestCode == START_BLUETOOTH){

                 Log.w(TAG, "Bluetooth Active");

                 showFriends();
             }
        }
    }

      public void showFriends()
    {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE, 300);
        startActivity(discoverableIntent);

        new StartServer().start();
    }


    class StartServer extends Thread

    {
        String TAG = "BluetoothLogs";
        BluetoothServerSocket bluetoothServerSocket;

        public StartServer()
        {

            try
            {
                this.bluetoothServerSocket = Master.bluetoothAdapter.listenUsingRfcommWithServiceRecord("Nikhil", UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));

            } catch (IOException e) {

                Log.w(TAG, "Device Not Available");
            }
        }

        @Override
        public void run() {

            BluetoothSocket bluetoothSocket = null;

              while(true)
            {
                try
                {
                    bluetoothSocket = bluetoothServerSocket.accept();

                } catch (IOException e) {

                    Log.w(TAG, "Connection Not Established");
                }

                if(bluetoothSocket != null)
                {
                    Log.w(TAG, "Other device acquired let's do something about it");

                    Master.bluetoothSocket = bluetoothSocket;


                    try {
                        Master.inputStream = bluetoothSocket.getInputStream();
                    } catch (IOException e) {
                        Log.w(TAG, "InputStream Not Acquired");
                    }

                    if(Master.inputStream != null){

                        Intent intent = new Intent(getApplicationContext(), SpeakerMicScreen.class);
                        intent.putExtra("Action", "Speaker");
                        startActivity(intent);

                    }

                    // do something with the socket;

                    break;
                }

            }



        }
    }


}
