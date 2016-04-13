package com.kaushiknath.viandsystem;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URI;

/**
 * Created by Kaushik Nath on 4/13/2016.
 */
public class VideoV extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vid);
        VideoView videoView = (VideoView) findViewById(R.id.vidi);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        String path= "android.resource://"+getPackageName()+"/"+R.raw.video1;
        Uri video = Uri.parse(path);
        videoView.setMediaController(mc);
        videoView.setVideoURI(video);
        videoView.start();
        while(videoView.isPlaying()){

        }
        path= "android.resource://"+getPackageName()+"/"+R.raw.video2;
        video = Uri.parse(path);
        videoView.setMediaController(mc);
        videoView.setVideoURI(video);
        videoView.start();
    }
}
