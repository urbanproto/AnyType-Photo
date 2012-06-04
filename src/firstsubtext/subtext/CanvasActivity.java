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
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
//each activity is a state. 
//this is the photo capture activity. It takes a picture 


public class CanvasActivity extends Activity implements OnTouchListener {

	private GridView letter_grid;
	private LetterView letter_view;
	private boolean two_finger = false;
	private int savedNum = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Canvas Call", "Entered ON Create");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Log.d("Canvas Call", "Set Content View");
		setContentView(R.layout.canvas);
		
		
		Log.d("Canvas Call", "Find Grid View");
		letter_grid = (GridView) findViewById(R.id.letter_grid);
		letter_grid.setAdapter(new LetterAdapter(this));

		letter_grid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				addToCanvas(position);
			}
		});

		letter_view = new LetterView(this);
		FrameLayout canvas = (FrameLayout) findViewById(R.id.canvas_frame);
		canvas.addView(letter_view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		letter_view.setOnTouchListener(this);
		
		Log.d("Canvas Call", "Ended Canvas");

		Button saveButton = (Button) findViewById(id.button_save_canvas);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveScreen();
			}
		});
		
		Button resetButton = (Button) findViewById(id.button_reset);
		resetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
			}
		});
		

	}
	
	private void saveScreen(){
		//save the picture

		File pictureFile = Globals.getOutputMediaFile(Globals.MEDIA_TYPE_IMAGE, "MyCanvas_" + (savedNum++) + ".png");
		if (pictureFile == null) {
			return;
		}


		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			Bitmap bmap = letter_view.getImageOut();
			bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
			Log.d("Capture Activity", "File Created");
			

		} catch (FileNotFoundException e) {
			Log.d("", "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d("", "Error accessing file: " + e.getMessage());
		}
		
	}
	
	public void reset(){
		Intent intent = new Intent(this, LoadActivity.class);
		startActivity(intent);
	}

	public void addToCanvas(int id) {
		letter_view.addLetter(id);
		letter_view.invalidate();
	}

	// Implement the OnTouchListener callback
	public boolean onTouch(View v, MotionEvent event) {
		Log.d("Touch", "Action: " + event.getAction());
		Log.d("Touch", "Action Index: " + event.getActionIndex());

		int selected;

		LetterView lv = (LetterView) v;


		if (event.getActionIndex() > 0) {
			two_finger = !two_finger;
			Log.d("Touch", "Two Finger: " + two_finger);

			if(event.getActionIndex() > 1){
				lv.removeCurrentLetter();
			}
			
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			selected = lv.locate((int) event.getX(), (int) event.getY());

			if (selected != lv.getCur() && lv.getCur() != -1)
				lv.deselect(lv.getCur());
			if (selected == -1)
				return true;
			lv.select(selected);

			// finger up - nothing selected
		} else if (event.getAction() == MotionEvent.ACTION_UP) {			
			
			selected = lv.getCur();
			if (selected != -1)
				lv.deselect(selected);

		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			selected = lv.getCur();
			if (selected == -1)
				return true;

			lv.updatePosition(event.getX(), event.getY());

			if (two_finger) {
				try {

					lv.updateScale(Globals.getScale(event.getX(0),
							event.getY(0), event.getX(1), event.getY(1)));
					
//					lv.setRotations(Globals.getRotation(event.getX(0),
//							event.getY(0), event.getX(1), event.getY(1)));
					
					float[] center = Globals.getCenter(event.getX(0),
							event.getY(0), event.getX(1), event.getY(1));
					lv.updatePosition(center[0], center[1]);

				} catch (IllegalArgumentException e) {
					Log.d("Matrix", "Exception Scale: " + e.getMessage());
					two_finger = false;
				}


			}

		}
		lv.invalidate();
		return true;

	}

}
