package tronbox.mike;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Master extends Application {

   public static BluetoothAdapter bluetoothAdapter;
   public static BluetoothDevice bluetoothDevice;
   public static BluetoothSocket bluetoothSocket;
   public static InputStream inputStream;
   public static OutputStream outputStream;
   public static Socket socket = new Socket();
   public static byte[] buffer = new byte[700];

   public static Boolean isScanning = false;

     public static void manageBluetoothSocket(BluetoothSocket socket, String Action)
   {
       bluetoothSocket = socket;

       try{

           if(Action.equals("Mic")){

               outputStream = bluetoothSocket.getOutputStream();

           } else if(Action.equals("Speaker")){

               inputStream = bluetoothSocket.getInputStream();

           }

       }catch (IOException ex){}

   }

}
