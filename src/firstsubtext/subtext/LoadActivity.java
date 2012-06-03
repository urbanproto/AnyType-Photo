package firstsubtext.subtext;


/**
 * This class starts all of the global variables.  It launches the application and moves on
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

import data.Letter;
import data.Shape;
import firstsubtext.subtext.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
public class LoadActivity extends Activity {

	protected static final String TAG = null;
	private static Globals environment;
	private static int stage = 0; // keeps a record of which shape we're
									// capturing

	public void loadShapes() {
		int ndx = 0;
		int count = 0;
		InputStream is;
		ArrayList x = new ArrayList();
		ArrayList y = new ArrayList();
		Iterator it;
		String[] data = new String[2];

		for (int id = 0; id < 5; id++) {
			x.clear();
			y.clear();
			
			try {

				String str = "";
				Log.d("Load Shape", "Switch " + id);

				if(id == 0) is = this.getResources().openRawResource(R.raw.sa);
				else if(id == 1) is = this.getResources().openRawResource(R.raw.sb);
				else if(id == 2) is = this.getResources().openRawResource(R.raw.sc);
				else if(id == 3)is = this.getResources().openRawResource(R.raw.sd);
				else is = this.getResources().openRawResource(R.raw.se);
				

				Log.d("Load Shape", "Shape " + id + "Loaded");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				if (is != null) {
					while ((str = reader.readLine()) != null) {
						Log.d("Load Shape", "Reading: " + str);
						data = str.split(",");
						x.add(Integer.valueOf(data[0].trim()));
						y.add(Integer.valueOf(data[1].trim()));
					}
				}
				is.close();

			} catch (IOException e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}

			// add the points to the arrays
			it = x.iterator();
			int[] x_points = new int[x.size()];
			ndx = 0;
			while (it.hasNext()) {
				x_points[ndx++] = (Integer) it.next();
			}

			it = y.iterator();
			int[] y_points = new int[y.size()];
			ndx = 0;
			while (it.hasNext()) {
				y_points[ndx++] = (Integer) it.next();
			}

			environment.getShape(id).setPoints(x_points, y_points);

		}

	}
	
	
	public void loadLetters() {
		int ndx = 0;
		int count = 0;
		InputStream is;
		ArrayList x = new ArrayList();
		ArrayList y = new ArrayList();
		ArrayList z = new ArrayList();
		ArrayList shape_ids = new ArrayList();
		Iterator it;
		String[] data = new String[4];

		for (int id = 0; id < 26; id++) {
			x.clear();
			y.clear();
			z.clear();
			shape_ids.clear();
			
			try {

				String str = "";
				Log.d("Load Letter", "Switch " + id);

				if(id == 0) is = this.getResources().openRawResource(R.raw.a);
				else if(id == 1) is = this.getResources().openRawResource(R.raw.b);
				else if(id == 2) is = this.getResources().openRawResource(R.raw.c);
				else if(id == 3)is = this.getResources().openRawResource(R.raw.d);
				else if(id == 4) is = this.getResources().openRawResource(R.raw.e);
				else if(id == 5) is = this.getResources().openRawResource(R.raw.f);
				else if(id == 6)is = this.getResources().openRawResource(R.raw.g);
				else if(id == 7) is = this.getResources().openRawResource(R.raw.h);
				else if(id == 8) is = this.getResources().openRawResource(R.raw.i);
				else if(id == 9)is = this.getResources().openRawResource(R.raw.j);
				else if(id == 10) is = this.getResources().openRawResource(R.raw.k);
				else if(id == 11) is = this.getResources().openRawResource(R.raw.l);
				else if(id == 12)is = this.getResources().openRawResource(R.raw.m);
				else if(id == 13) is = this.getResources().openRawResource(R.raw.n);
				else if(id == 14) is = this.getResources().openRawResource(R.raw.o);
				else if(id == 15)is = this.getResources().openRawResource(R.raw.p);
				else if(id == 16) is = this.getResources().openRawResource(R.raw.q);
				else if(id == 17) is = this.getResources().openRawResource(R.raw.r);
				else if(id == 18)is = this.getResources().openRawResource(R.raw.s);
				else if(id == 19) is = this.getResources().openRawResource(R.raw.t);
				else if(id == 20) is = this.getResources().openRawResource(R.raw.u);
				else if(id == 21)is = this.getResources().openRawResource(R.raw.v);
				else if(id == 22) is = this.getResources().openRawResource(R.raw.w);
				else if(id == 23) is = this.getResources().openRawResource(R.raw.x);
				else if(id == 24)is = this.getResources().openRawResource(R.raw.y);
				else is = this.getResources().openRawResource(R.raw.z);
				

				Log.d("Load Letter", "Letter " + id + "Loaded");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				if (is != null) {
					while ((str = reader.readLine()) != null) {
						Log.d("Load Shape", "Reading: " + str);
						data = str.split(",");
						x.add(Integer.valueOf(data[0].trim()));
						y.add(Integer.valueOf(data[1].trim()));
						z.add(Float.valueOf(data[2].trim()));
						shape_ids.add(Integer.valueOf(data[3].trim()));

					}
				}
				is.close();

			} catch (IOException e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}

			// add the points to the arrays
			it = x.iterator();
			int[] x_points = new int[x.size()];
			ndx = 0;
			while (it.hasNext()) {
				x_points[ndx++] = (Integer) it.next();
			}

			
			it = y.iterator();
			int[] y_points = new int[y.size()];
			ndx = 0;
			while (it.hasNext()) {
				y_points[ndx++] = (Integer) it.next();
			}
			
			
			it = z.iterator();
			float[] r = new float[z.size()];
			ndx = 0;
			while (it.hasNext()) {
				r[ndx++] = (Float) it.next();
			}
			
			it = shape_ids.iterator();
			int[] shape_info = new int[shape_ids.size()];
			ndx = 0;
			while (it.hasNext()) {
				shape_info[ndx++] = (Integer) it.next();
			}

			environment.getLetter(id).setInfo(x_points, y_points, r, shape_info);

		}

	}
	
	public void cleanUpShapes(){
		Rect[] skews = new Rect[Globals.shapes.length];
		Letter l;
		Shape s;
		int[] shapes, x, y;
		int correct_x,correct_y;
		
		//make a list of all of the offsets of the points
		for(int i = 0; i < Globals.shapes.length; i++){
			Rect r = Globals.shapes[i].getBounds();
			skews[i] = r;
		    correct_x = -1*r.left;
			correct_y = -1*r.top;
			Globals.shapes[i].offset(correct_x, correct_y);
		}
		
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		environment = new Globals(
				new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));

		loadShapes();
		loadLetters();
		cleanUpShapes();

		setContentView(R.layout.main);
		Log.d("Load Activity", "Loaded");
		Intent intent = new Intent(this, CanvasActivity.class);
		////Intent intent = new Intent(this, CaptureActivity.class);
		startActivity(intent);


	}

	@Override
	public void onRestart() {

	}

	@Override
	protected void onPause() {
		super.onPause();
	}



}
