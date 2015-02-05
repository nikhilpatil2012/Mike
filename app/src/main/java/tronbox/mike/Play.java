package tronbox.mike;


import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;

public class Play extends AsyncTask<Void, Void, Void> implements CommonMetaData{


    @Override
    protected Void doInBackground(Void... voids) {

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz,channelConfig, audioFormat, bufferSizeInBytes,AudioTrack.MODE_STREAM);
        audioTrack.play();


        while(true){

            try {

                Master.inputStream.read(buffer);

                if(buffer != null){

                    audioTrack.write(buffer, 0, buffer.length);
                }


            } catch (IOException e) {
                Log.w("BluetoothLogs", "Unable to Read "+e.getMessage());
                break;
            }
        }


        return null;
    }


}
