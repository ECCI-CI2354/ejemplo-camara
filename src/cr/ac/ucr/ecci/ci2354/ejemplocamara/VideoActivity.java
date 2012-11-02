package cr.ac.ucr.ecci.ci2354.ejemplocamara;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class VideoActivity extends Activity implements SurfaceHolder.Callback {
    private MediaRecorder mr;

    File videoFile;

    SurfaceView videoSurface;

    SurfaceHolder mholder;

    Camera mCamera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        mholder = videoSurface.getHolder();
        mholder.addCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_video, menu);
        return true;
    }

    @Override
    protected void onPause() {
        mr.release();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mr = new MediaRecorder();
        mCamera = Camera.open();
        mCamera.unlock();
    }

    public void record(View view) {

        mr.setVideoEncoder(MediaRecorder.VideoSource.DEFAULT);
        mr.setAudioEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mr.setPreviewDisplay(mholder.getSurface());
        mr.setCamera(mCamera);
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        videoFile = new File(root, System.currentTimeMillis() + ".3gpp");
        mr.setOutputFile(videoFile.getAbsolutePath());
        try {
            mr.prepare();
        } catch (IllegalStateException e) {
            Log.e("", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
        }
        mr.start();
    }

    public void stop(View view) {
        mr.stop();
        mr.reset();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

}
