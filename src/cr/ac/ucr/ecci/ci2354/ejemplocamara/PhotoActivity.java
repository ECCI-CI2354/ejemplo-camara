package cr.ac.ucr.ecci.ci2354.ejemplocamara;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PhotoActivity extends Activity implements SurfaceHolder.Callback {
	private static final String TAG = "PhotoCamera";
	private Camera camera;
	private SurfaceHolder mHolder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        SurfaceView cameraview = (SurfaceView) findViewById(R.id.cameraView);
        mHolder = cameraview.getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo, menu);
        return true;
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
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(mHolder);
			camera.startPreview();
			//TODO dibujar sobre el preview si es necesario (realidad aumentada)
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		camera.stopPreview();
	}

	@Override
	protected void onPause() {
		super.onPause();
		camera.stopPreview();
		camera.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		camera = Camera.open();
	}

	
}
