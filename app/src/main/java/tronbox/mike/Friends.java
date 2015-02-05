package tronbox.mike;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Friends extends Fragment{

    private ListView listView;
    private FriendsAdapter friendsAdapter;

    private ArrayList<BluetoothDevice> test;
    private IntentFilter intentFilter;
    private Button scanButton;
    private AnimationDrawable scanningAnimationDrawable;
    private TextView searchingMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.friends_list, container, false);

        searchingMessage = (TextView) view.findViewById(android.R.id.empty);
        scanningAnimationDrawable = (AnimationDrawable)searchingMessage.getCompoundDrawables()[1];

        scanButton = (Button)view.findViewById(R.id.scan_bottom);

        listView = (ListView)view.findViewById(R.id.friends_list);

        test = new ArrayList<BluetoothDevice>();

        friendsAdapter = new FriendsAdapter(getActivity().getApplicationContext(), R.layout.friend_list_item, test);

        listView.setAdapter(friendsAdapter);

          if(Master.bluetoothAdapter != null)
        {
            test.addAll(Master.bluetoothAdapter.getBondedDevices());
            friendsAdapter.notifyDataSetChanged();

            intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            getActivity().getApplicationContext().registerReceiver(receiver, intentFilter);
            new Scan().execute();
        }


        return view;
    }

       class FriendsAdapter extends ArrayAdapter<BluetoothDevice>
     {

        private ArrayList<BluetoothDevice> list;
        private int resource;

           public FriendsAdapter(Context context, int resource, ArrayList<BluetoothDevice> list)
         {
             super(context, resource, list);

             this.list = list;
             this.resource = resource;
         }

         @Override
         public View getView(final int position, View convertView, ViewGroup parent) {

             View view = getActivity().getLayoutInflater().inflate(resource, parent, false);

             ((TextView)view.findViewById(R.id.friend_name)).setText(list.get(position).getName());

             ((Button)view.findViewById(R.id.friend_invite)).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     Master.bluetoothDevice = list.get(position);

                     new AcceptConnection().start();

                 }
             });

             scanButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     new Scan().execute();
                     Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();

                 }
             });

             return view;
         }
     }

    // Broadcast Reciever, when new device is found.

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(!test.contains(device)){
                    test.add(device);
                }

                friendsAdapter.notifyDataSetChanged();

            }   else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
              {

                  searchingMessage.setVisibility(View.VISIBLE);
                  scanButton.setEnabled(false);
                  scanningAnimationDrawable.start();

//                  Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();

              }  else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
               {

                   scanningAnimationDrawable.stop();
                   searchingMessage.setVisibility(View.INVISIBLE);
                   scanButton.setEnabled(true);
             //      Toast.makeText(getActivity(), "Finished", Toast.LENGTH_SHORT).show();
               }
    }};

    class Scan extends AsyncTask<Void, Void, Void>

    {

        @Override
        protected Void doInBackground(Void... voids) {

            Master.bluetoothAdapter.startDiscovery();

            return null;
        }
    }

    class AcceptConnection extends Thread
    {
        BluetoothSocket temp = null;
        private String TAG = "BluetoothLogs";

          public AcceptConnection()

        {
            try {

                temp = Master.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));

            } catch (IOException e) {
                Log.w(TAG, "Connection Not Established");
            }
        }

        @Override
        public void run() {

            try {

                temp.connect();



            } catch (IOException e) {

                Log.w(TAG, "Connection Not Established");
            }


            if(temp.isConnected()){
                Log.w(TAG, "Connected to the Device");

                Master.bluetoothSocket = temp;


                try {
                    Master.outputStream = temp.getOutputStream();
                } catch (IOException e) {
                    Log.w(TAG, "InputStream Not Acquired");
                }

                if(Master.outputStream != null){

                    Intent intent = new Intent(getActivity().getApplicationContext(), SpeakerMicScreen.class);
                    intent.putExtra("Action", "Mic");
                    startActivity(intent);
                }


            }

        }
    }


}




