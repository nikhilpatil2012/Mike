package tronbox.mike;


import android.media.AudioRecord;
import android.media.audiofx.NoiseSuppressor;
import android.os.AsyncTask;
import android.util.Log;

public class Record extends AsyncTask<Void, Void, Void> implements CommonMetaData{

    private volatile boolean check = true;

    @Override
    protected Void doInBackground(Void... voids) {


        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);

        NoiseSuppressor suppressor = NoiseSuppressor.create(audioRecord.getAudioSessionId());
        suppressor.setEnabled(true);

        if(audioRecord.getRecordingState() == AudioRecord.STATE_INITIALIZED){

            Log.w("AudioRecorder", "State Initialized");

            try {

                audioRecord.startRecording();

                  while(true)
                {

                    if(check == true){

                        audioRecord.read(buffer, 0, bufferSizeInBytes);
                        Master.outputStream.write(buffer);

                    }
                }

            } catch (Throwable ex){

                Log.w("AudioRecorder", "Recording Error "+ex.getMessage());

            }
        }

        return null;
    }

     public void setCheck(boolean value)
    {
        check = value;
    }


}
