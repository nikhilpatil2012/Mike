package tronbox.mike;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public interface CommonMetaData {

    int audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
    int sampleRateInHz = 8000;
    int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    byte[] buffer = new byte[bufferSizeInBytes];

}
