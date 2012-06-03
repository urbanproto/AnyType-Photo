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
public class CanvasActivity extends Activity {

	private GridView letter_grid;
	private LetterView letter_view;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//for now
		Globals.buildLetters();
		setContentView(R.layout.canvas);
		
	
	    letter_grid = (GridView) findViewById(R.id.letter_grid);
	    letter_grid.setAdapter(new LetterAdapter(this));

	    
	    letter_grid.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Toast.makeText(CanvasActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	addToCanvas(position);
	        }
	    });
	    
	   
	    
	   
	}
	
	public void addToCanvas(int id){
		LetterView letter_view = new LetterView(this, Globals.letters[id]);
		FrameLayout canvas = (FrameLayout) findViewById(R.id.canvas_frame);
		canvas.addView(letter_view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		
		
		
//		 letter_view.setOnTouchListener(new OnTouchListener()
//		    {
//		        @Override
//		        public boolean onTouch(View v, MotionEvent event)
//		        {
//		            // now you know coordinates of touch
//		            // store them
//		            sideIndexX = event.getX();
//		            sideIndexY = event.getY();
//
//		            doStuff();
//
//		            return false;
//		        }
//		    });

		
	}


	
	


}
