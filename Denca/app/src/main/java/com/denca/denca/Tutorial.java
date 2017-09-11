package com.denca.denca;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by mdnah on 9/9/2017.
 */

public class Tutorial extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        VideoView tutorialVid = (VideoView) findViewById(R.id.tutoialVid);


        String path = "android.resource://" + getPackageName() + "/" + R.raw.sample;
        Uri uri = Uri.parse(path);
        tutorialVid.setVideoURI(uri);
        tutorialVid.start();


        TextView cameraBtn = (TextView) findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tutorial.this, Camera.class);
                startActivity(intent);
            }
        });

    }
}
