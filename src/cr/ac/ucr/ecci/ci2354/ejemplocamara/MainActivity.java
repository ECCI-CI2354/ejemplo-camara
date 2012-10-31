package cr.ac.ucr.ecci.ci2354.ejemplocamara;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final int PHOTO_REQUEST_CODE = 1;
	private static final int VIDEO_REQUEST_CODE = 2;

	private static final int MEDIA_TYPE_IMAGE = 0;

	private static final int MEDIA_TYPE_VIDEO = 1;
	private File mediaFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void takeVideo(View view) {
		startActivity(new Intent(this, VideoActivity.class));
	}

	public void takePhoto(View view) {
		startActivity(new Intent(this, PhotoActivity.class));
	}

	public void useCamera(View view) {
		openChooseImageIntent();
	}

	public void useVideoCamera(View view) {
		openChooseVideoIntent();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				String filename = null;
				Uri mSelectedImage = null;
				if (data != null) {
					mSelectedImage = data.getData();
					String[] columns = { MediaStore.Images.Media.DATA };
					Cursor imageCursor = getContentResolver().query(
							mSelectedImage, columns, null, null, null);
					if (imageCursor.moveToFirst()) {
						filename = imageCursor.getString(imageCursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
						imageCursor.close();
					} else {
						setResult(RESULT_CANCELED);
						finish();
					}

				} else {
					filename = mediaFile.getAbsolutePath();
				}
				Log.d("", "Photo saved at:" + filename);
				Toast.makeText(this, "Photo saved at:" + filename,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.photo_error, Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case VIDEO_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				
			} else {
				
			}
			break;
		default:
			break;
		}
	}

	private void openChooseImageIntent() {
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		mediaFile = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		captureIntent
				.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				getString(R.string.select_source));

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));

		startActivityForResult(chooserIntent, PHOTO_REQUEST_CODE);
	}
	private void openChooseVideoIntent() {
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		mediaFile = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
		captureIntent
				.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("video/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				getString(R.string.select_source));

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));

		startActivityForResult(chooserIntent, VIDEO_REQUEST_CODE);
	}

	private File getOutputMediaFileUri(int mediaTypeImage) {
		File root = null;
		String extension = ".jpg";

		switch (mediaTypeImage) {
		case MEDIA_TYPE_IMAGE:
			root = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			break;
		case MEDIA_TYPE_VIDEO:
			root = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
			extension = ".mp4";
		}

		return new File(root, System.currentTimeMillis() + extension);
	}
}
