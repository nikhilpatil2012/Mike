package tronbox.mike;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class SpeakerMicScreen extends Activity implements CommonMetaData {

    private Button start, disconnect;
    private ImageView imageView;
    private String action;
    private boolean flipFlop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker_mic_screen);

        start = (Button)findViewById(R.id.start);
        disconnect = (Button)findViewById(R.id.disconnect);
        imageView = (ImageView)findViewById(R.id.speaker_mic);


        if(getIntent().getExtras() != null && getIntent().hasExtra("Action")){

            action = getIntent().getExtras().getString("Action");

            if(action != null){

                if(action.equals("Mic")){

                    imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mic));

                } else if(action.equals("Speaker")){

                    start.setEnabled(false);
                    disconnect.setVisibility(View.GONE);
                    start.setVisibility(View.GONE);

                    imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speaker));

                }
            }
        }

        Intent intent = new Intent(getApplicationContext(), ConnectService.class);
        intent.putExtra("Action", action);
        startService(intent);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(flipFlop == false){

                sendBroadcast(new Intent("Start_Record"));
                start.setText("Stop");
                start.setBackgroundResource(R.drawable.oval_button_red_shape);
                flipFlop = true;

            }else if(flipFlop == true){

                sendBroadcast(new Intent("Stop_Record"));
                start.setText("Start");
                start.setBackgroundResource(R.drawable.oval_button__green_shape);
                flipFlop = false;
            }


            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Master.bluetoothSocket.close();

                    onBackPressed();


                } catch (IOException e) {

                    Log.w("SpeakerScreen", e.getMessage());
                }

            }
        });
    }

}
