package firstsubtext.subtext;

/***
 * This class draws a shape to the screen and allows the user 
 * to capture that shape
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import data.Shape;
import firstsubtext.subtext.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.ViewGroup.LayoutParams;

//each activity is a state. 
//this is the photo capture activity. It takes a picture 
public class CaptureActivity extends Activity {

	protected static final String TAG = null;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Camera mCamera;
	private CameraPreview mPreview;
	private static DrawShapeOnTop shapeView;
	private FrameLayout preview;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("Canvas Call", "On Create Called");

		//looks at the current stage and builds any letters it can
		Globals.buildLetters();
		
		Log.d("Canvas Call", "Returned From Build Letters");

		//if we've captured all the images, move to the canvas stage
		if(Globals.stage > 4){

			Log.d("Canvas Call", "Create Intent");	
			Intent intent = new Intent(this, CanvasActivity.class);
			startActivity(intent);
			Log.d("Canvas Call", "Reset Stage Pre" + Globals.stage);

			
			Globals.resetStage();
			Log.d("Canvas Call", "Reset Stage Post" + Globals.stage);


		}else{
			Log.d("Canvas Call", "In Else");

			
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cameracapture);
		

		// Add a listener to the Capture button
		Button captureButton = (Button) findViewById(id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(null, null, mPicture);
			}
		});

		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		shapeView = new DrawShapeOnTop(this, Globals.getStageShape(), false);

		preview = (FrameLayout) findViewById(id.camera_preview);
		preview.addView(mPreview);
		preview.addView(shapeView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		}
	}

	@Override
	public void onRestart() {
		Log.d("Capture Activity", "Restart Called");

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("Capture Activity", "onPause Called, Camera Released");
		releaseCamera(); // release the camera immediately on pause event
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
			Parameters parameters = c.getParameters();
			
			
			/*List<String> focusModes = parameters.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
			{
			    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			}
			
			parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
			parameters.setAutoExposureLock(true);
			
			Log.d("Camera Settings: Antibanding: ", parameters.getAntibanding());
			Log.d("Camera Settings: Focus mode: ", parameters.getFocusMode());
			Log.d("Camera Settings: White Balance: ", parameters.getWhiteBalance());
			Log.d("Camera Settings: Scene Mode: ", parameters.getSceneMode());
			*/
			parameters.setPictureSize(shapeView.getWidth(), shapeView.getHeight());
	        c.setParameters(parameters);
			
			
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			System.out.println("camera does not exist or is in use");
		}
		return c; // returns null if camera is unavailable
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {

		// Create a media file name
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(Globals.getPath() + File.separator
					+ "IMG_" + Integer.toString(Globals.stage) + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(Globals.getPath() + File.separator
					+ "VID_" + Integer.toString(Globals.stage) + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	// go to the next screen - or - go to the edit screen and
	// return when finished
	private void nextScreen() {
		Log.d("Capture Activity", "Create Intent");

		Intent intent = new Intent(this, ViewCaptureActivity.class);
		//startActivityForResult(intent, 1);
		startActivity(intent);
	}

//	// the page is loaded before this is called
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// If the request went well (OK) and the request was
//		// PICK_CONTACT_REQUEST
//		if (resultCode == 1) {
//			Globals.nextStage();
//			Log.d("Capture Activity", "ADDING ONE TO STAGE");
//
//			preview.removeView(shapeView);
//			shapeView = new DrawShapeOnTop(this, Globals.getStageShape());
//			preview.addView(shapeView, new LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//
//		}
//	}

	private PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("Capture Activity", "Picture Taken");

			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null) {
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				Log.d("Capture Activity", "File Created");
				

			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}

			nextScreen();
		}

	};

}
