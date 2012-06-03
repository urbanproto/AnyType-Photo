package firstsubtext.subtext;

/***
 * This class shows the captured shape just after it is photographed
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import firstsubtext.subtext.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
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
public class ViewCaptureActivity extends Activity {
	

	protected static final String TAG = "ViewCaptureActivity";
	private DrawShapeOnTop shapeView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.viewcapture);

		Log.d("View Activity", "View Activity Loaded");

		
		// add accept button listener
		Button acceptButton = (Button) findViewById(id.button_accept);
		acceptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(1); //return true for accepted
			
				//save the picture
				Log.d("Capture Activity", "Picture Taken");

				File pictureFile = Globals.getOutputMediaFile(Globals.MEDIA_TYPE_IMAGE, "IMG_" + Integer.toString(Globals.stage) + "_CROP.png");
				if (pictureFile == null) {
					return;
				}

				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					Bitmap bmap = shapeView.getShapeImageOut();
					bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					fos.close();
					Log.d("Capture Activity", "File Created");
					

				} catch (FileNotFoundException e) {
					Log.d(TAG, "File not found: " + e.getMessage());
				} catch (IOException e) {
					Log.d(TAG, "Error accessing file: " + e.getMessage());
				}
				
				
				//end save the picture
				Globals.nextStage();
				
				finish();
			}
		});

		// add accept button listener
		Button rejectButton = (Button) findViewById(id.button_reject);
		rejectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				setResult(0);  //return false to retake
				finish();
			}
		});

		// Create our Preview view and set it as the content of our activity.
		shapeView = new DrawShapeOnTop(this, Globals.getStageShape(), true);
		FrameLayout preview = (FrameLayout) findViewById(id.camera_preview);
		// preview.addView(mPreview);
		preview.addView(shapeView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

	}
	

	
	


}
