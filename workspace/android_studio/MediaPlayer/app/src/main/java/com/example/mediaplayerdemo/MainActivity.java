package com.example.mediaplayerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private TextureView textureView;
    private Surface surface;
    private MediaPlayer mPlayer;

    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = findViewById(R.id.textureview);
        btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.start();
            }
        });
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                Log.e("yqtest", "onSurfaceTextureAvailable");
                surface = new Surface(surfaceTexture);
//                play();
                mPlayer.setSurface(surface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
        try {
            mPlayer = new MediaPlayer();
//            Uri uri = Uri.parse("https://1008-24-1.vod.tv.itc.cn/sohu/v1/TmwCTmwmoAIlq8xbqtXeDAW4y8wt8VALqMw3gVX1lm47fFoGRMNiNw.mp4?k=SNM2Gr&p=jWlvzSxi0SoUopsWj9jLtUJloL3qxCOzafNIgMwifS9gfDNAqMkIl5lavjmX95mo3ad4ilEs3lQSHafSA3FkkwGOmJdoChOqLrdjll5amkCjSYNtwG8EA3Dj9bWj9lvzSr7OS5vo9smopq6qmJlOpr3qLqF0SFvOfPCOLodoUP3hR5vzSvmhRYAtUxINT17wmfVZDyHfJXXvm12ZDeSotxcyLNS0p0cWho2yt8BZMK7qhsC0Lb70OCHlVPdyLKH0mcA5mE2oOXGgGcQgm8VRDcsWGeHWJ1tlBWsZ8KdNt9eDS4LTS0XgpCDPVoBgAkHqM14wmfdRDS&r=TmI20LscWOoUgt8ISCBoU35omo7saqiSp55EwF0t977sC7BXLqWa3OuovI7TBoCNLfcWh1sZYoioMycY&q=OpCihW7IWhodRDWXvmfCyY2OfJ1HfhdslGAs5Gv4fDo2ZDvtfhbtfOyswm4cWJ6XvmscWY");
            mPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            //让MediaPlayer和TextureView进行视频画面的结合

            //设置监听
            mPlayer.setOnBufferingUpdateListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setScreenOnWhilePlaying(true);//在视频播放的时候保持屏幕的高亮
            //异步准备
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play() {
        try {
            mPlayer = new MediaPlayer();
//            Uri uri = Uri.parse("https://1008-24-1.vod.tv.itc.cn/sohu/v1/TmwCTmwmoAIlq8xbqtXeDAW4y8wt8VALqMw3gVX1lm47fFoGRMNiNw.mp4?k=SNM2Gr&p=jWlvzSxi0SoUopsWj9jLtUJloL3qxCOzafNIgMwifS9gfDNAqMkIl5lavjmX95mo3ad4ilEs3lQSHafSA3FkkwGOmJdoChOqLrdjll5amkCjSYNtwG8EA3Dj9bWj9lvzSr7OS5vo9smopq6qmJlOpr3qLqF0SFvOfPCOLodoUP3hR5vzSvmhRYAtUxINT17wmfVZDyHfJXXvm12ZDeSotxcyLNS0p0cWho2yt8BZMK7qhsC0Lb70OCHlVPdyLKH0mcA5mE2oOXGgGcQgm8VRDcsWGeHWJ1tlBWsZ8KdNt9eDS4LTS0XgpCDPVoBgAkHqM14wmfdRDS&r=TmI20LscWOoUgt8ISCBoU35omo7saqiSp55EwF0t977sC7BXLqWa3OuovI7TBoCNLfcWh1sZYoioMycY&q=OpCihW7IWhodRDWXvmfCyY2OfJ1HfhdslGAs5Gv4fDo2ZDvtfhbtfOyswm4cWJ6XvmscWY");
            mPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            //让MediaPlayer和TextureView进行视频画面的结合
            mPlayer.setSurface(surface);
            //设置监听
            mPlayer.setOnBufferingUpdateListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setScreenOnWhilePlaying(true);//在视频播放的时候保持屏幕的高亮
            //异步准备
            mPlayer.prepareAsync();
            Log.e("yqtest", "prepareAsync");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.e("yqtest", "onBufferingUpdate, percent: " + i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("yqtest", "onCompletion");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("yqtest", "onError");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e("yqtest", "onPrepared");
        mediaPlayer.start();
    }
}
