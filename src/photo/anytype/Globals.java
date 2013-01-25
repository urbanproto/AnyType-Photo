package photo.anytype;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.lang.Math;

import data.*;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;


public class Globals {
	static String timeStamp;
	static String startDateTime;
	static File mediaStorageDir;

	static Shape[] shapes;
	static Letter[] letters;
	static boolean[] existing_letters;
	static int stage;
	public static float shapeStretch = 2.0f;
	public static float shapeShrink = 0.5f; //used to make video bitmaps smaller
	public static int letter_size = 600;
	static int grab_num = 0;
	static String base_dir_name;

	static int background_alpha = 150;
	static int line_num = 0;
	static String save_string = "";
	static boolean edit = false;

	//static BuildLettersTask build_thread = new BuildLettersTask();

	
	static boolean playback_mode = false;
	static int force_stage = 0;
	static int force_letter = 0;
	
	static Point screen_size;
	static Point picture_size = new Point();
	public static Point preview_size = new Point();
	static double aspect_ratio;
	static float screen_density;
	
//	static double longitude;
//	static double latitude;
//	static LocationManager lm;
//		

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private static float[] lastfinger1 = new float[2];
	private static float[] lastfinger2 = new float[2];
	private static double lastSpan = -1;
	
	static LetterView saved_lv = null;
	static LetterEditView saved_lev = null;
  
	

	
	
	public Globals(Point screen, float density, Context context) {
		screen_size = new Point(screen);
		screen_density = density; //pixels per 1 pixel
		
		//lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 

		startDateTime = new SimpleDateFormat("yyyy : MM : dd : HH : mm : ss").format(new Date());

		
	   /* LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		        longitude = location.getLongitude();
		        latitude = location.getLatitude();
		    }

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
		};
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
		*/
		
		
		existing_letters = new boolean[26];
		for (int i = 0; i < existing_letters.length; i++) {
			existing_letters[i] = false;
		}


		// Load all of the shapes into an array for referencing into an array
		shapes = new Shape[5];
		for (int i = 0; i < 5; i++) {
			shapes[i] = new Shape(i);
		}

		letters = new Letter[26];
		for (int i = 0; i < 26; i++) {
			letters[i] = new Letter(i);
		}

		lastfinger1[0] = -1;
		lastfinger1[1] = -1;
		lastfinger2[0] = -1;
		lastfinger2[1] = -1;
		lastSpan = -1;

		stage = 0;
		

	
		
		try {
			
			Camera mCamera = Camera.open(); // attempt to get a Camera instance	
			Parameters parameters = mCamera.getParameters();
			

			
			int jpegqual = parameters.getJpegQuality();
			Log.d("Qual", "JPEG quality "+jpegqual);
			parameters.setJpegQuality(75);

			
			//set the preview size to be wholly contained in the view
			List<Size> preview_sizes = parameters.getSupportedPreviewSizes();
			Iterator it = preview_sizes.iterator();
	    	while(it.hasNext()){
	    		Size s = (Size) it.next();
	    		if(s.width <= screen_size.x*density && s.height <= screen_size.y*density)
	    			preview_size.set(s.width, s.height);			
	    	}
			Log.d("Tap", "Selectcted Preview Size: "+Globals.preview_size.x+" "+Globals.preview_size.y);

			Globals.aspect_ratio = (double)Globals.preview_size.x/(double)Globals.preview_size.y;
			Log.d("Tap", "Aspect Ratio: "+Globals.aspect_ratio);
	    	
	    	//get the largest format that has the same aspect ratio
			List<Size> picture_sizes = parameters.getSupportedPictureSizes();
			it = picture_sizes.iterator();
	    	while(it.hasNext()){
	    		Size s = (Size) it.next();
				Log.d("Tap", "Picture Size "+s.width+" "+s.height);
//				if(s.width <= screen_size.x*density && s.height <= screen_size.y*density){
//	    			picture_size.set(s.width, s.height);	
//					Log.d("Tap", "Selected Picture Size "+s.width+" "+s.height);
//
//				}
				
				
	    		if(Math.abs(((double)s.width / (double)s.height) - Globals.aspect_ratio) < .001)
					Log.d("Tap", "Selected Picture Size "+s.width+" "+s.height);
	    			Globals.picture_size.set(s.width, s.height);		

	    	}
	    	
			mCamera.release();

		} catch (Exception e) {
			Log.d("Tap", "No Camera Exists");	

		}

	}
	
	public static boolean createNewDirectory(String time){
		timeStamp =time;
		mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"SI_" + timeStamp);
		
		base_dir_name = "SI_" + timeStamp;
		//base_dir_name = "testdir";
	

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return false;
			}
		}
		return true;
	}
	
	public static void clearFingerData(){
		lastfinger1[0] = -1;
		lastfinger1[1] = -1;
		lastfinger2[0] = -1;
		lastfinger2[1] = -1;
		lastSpan = -1;

	}
	
	public static boolean renameDirectory(String s){
		boolean success = false;
		File file = new File(getTestPath());
		File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),s);
		if(!file2.exists()){
		 success = file.renameTo(file2);
		}
		
		if(success) base_dir_name = s;
		return success;
	}
	

	public static String getStagePhotoPath(int stageid) {
		return (getTestPath() + File.separator + "IMG_" + Integer.toString(stageid) + ".jpg");
	}

	

	public static boolean buildLetters(int stage) {
				
		int w = letter_size; // this is based on 2 * the bounding box size
		int h = letter_size;
		int[] offset;
		boolean make_letter;

		for (int i = 0; i < letters.length; i++) {

			make_letter = true;

			// make sure we have the shapes and are able to create this
			int[] shape_ids = letters[i].getShapeIds();
			for (int k = 0; k < shape_ids.length; k++) {
				if (shape_ids[k] > stage) {
					make_letter = false;
					break;
				}
			}

			if (make_letter && !existing_letters[i]) {
				
				existing_letters[i] = true;
				Log.d("Thead", "Make Letter "+intToChar(i));
				
				Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				Canvas c = new Canvas(bitmap);

				c.drawColor(Color.TRANSPARENT);
				int[] x_points = letters[i].getXPoints();
				int[] y_points = letters[i].getYPoints();
				float[] rots = letters[i].getRotations();

				for (int j = 0; j < x_points.length; j++) {
					
					offset = shapes[shape_ids[j]].getOffset();
					c.save();

					c.translate(x_points[j] * Globals.shapeStretch, y_points[j]
							* Globals.shapeStretch);
					c.rotate((int) Math.toDegrees(rots[j]));
					c.translate(offset[0] * Globals.shapeStretch, offset[1]
							* Globals.shapeStretch);

					File f = new File(getTestPath()
							+ File.separator + "IMG_"
							+ Integer.toString(shape_ids[j]) + "_CROP.png");
					
					Log.d("Thead", "File Exists "+f.exists()+ "Path: "+f.getPath());

					
					Bitmap bmap = Globals.decodeSampledBitmapFromResource(f,letter_size, letter_size);
					c.drawBitmap(bmap, new Matrix(), null);
					c.restore();
					
					bmap.recycle();

				}
				
				x_points = null;
				y_points = null;
				rots = null;

				try {

					Bitmap out = Bitmap.createBitmap(bitmap, 0, 0, w, h,
							new Matrix(), false);
					File pictureFile = Globals.getOutputMediaFile(
							MEDIA_TYPE_IMAGE, intToChar(i) + ".png");

					if (pictureFile == null) {
						return false;
					}

					try {
						FileOutputStream fos = new FileOutputStream(pictureFile);
						out.compress(Bitmap.CompressFormat.PNG, 100, fos);
						fos.close();

					} catch (FileNotFoundException e) {
						Log.d("Canvas", "File not found: " + e.getMessage());
					} catch (IOException e) {
						Log.d("Canvas",
								"Error accessing file: " + e.getMessage());
					}
					out.recycle();

				} catch (IllegalArgumentException e) {
					Log.d("Canvas", "Illegal Arg" + e.getMessage());
				}
				
				bitmap.recycle();

			}
		}
		
		Log.d("Thead", "Exit Build Letters");
		System.gc();
		
		
	
		return true;
	}
	
	public static int buildLetter(int i) {
		
		int w = letter_size; // this is based on 2 * the bounding box size
		int h = letter_size;
		int[] offset;
		int[] shape_ids = letters[i].getShapeIds();
		int[] x_points = letters[i].getXPoints();
		int[] y_points = letters[i].getYPoints();
		float[] rots = letters[i].getRotations();


		
		Log.d("Thead", "Make Letter "+intToChar(i));
		
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);

		c.drawColor(Color.TRANSPARENT);

		for (int j = 0; j < x_points.length; j++) {
			
			offset = shapes[shape_ids[j]].getOffset();
			c.save();

			c.translate(x_points[j] * Globals.shapeStretch, y_points[j]
					* Globals.shapeStretch);
			c.rotate((int) Math.toDegrees(rots[j]));
			c.translate(offset[0] * Globals.shapeStretch, offset[1]
					* Globals.shapeStretch);

			File f = new File(getTestPath()
					+ File.separator + "IMG_"
					+ Integer.toString(shape_ids[j]) + "_CROP.png");
			
			Log.d("Thead", "File Exists "+f.exists()+ "Path: "+f.getPath());

			
			Bitmap bmap = Globals.decodeSampledBitmapFromResource(f,letter_size, letter_size);
			c.drawBitmap(bmap, new Matrix(), null);
			c.restore();
			
			bmap.recycle();

		}
		
		x_points = null;
		y_points = null;
		rots = null;

		try {

			Bitmap out = Bitmap.createBitmap(bitmap, 0, 0, w, h,
					new Matrix(), false);
			File pictureFile = Globals.getOutputMediaFile(
					MEDIA_TYPE_IMAGE, intToChar(i) + ".png");

//					if (pictureFile == null) {
//						return false;
//					}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				out.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d("Canvas", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("Canvas",
						"Error accessing file: " + e.getMessage());
			}
			out.recycle();

		} catch (IllegalArgumentException e) {
			Log.d("Canvas", "Illegal Arg" + e.getMessage());
		}
		
		bitmap.recycle();

	
	
		
		System.gc();
		
		
	
		return (int) ((i+1)*((float)100/(float)26));
	}
	
	

	public static Shape getStageShape() {
		return shapes[stage];
	}

	public static void nextStage() {
		stage++;
	}

	public static String getTime() {
		return timeStamp;
	}

//	public static String getPath() {
//		return mediaStorageDir.getPath();
//	}
	
	public static String getPicturesPath() {
		
		File picturesPath = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");
		
//		if (!mediaStorageDir.exists()) {
//			if (!mediaStorageDir.mkdirs()) {
//				Log.d("MyCameraApp", "failed to create directory");
//			}
//		}
		
		return picturesPath.getPath();
	}

	public static String getTestPath() {
		//return getPath();
		if(base_dir_name != null){
		File testDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				base_dir_name);
		if(testDir.exists()) return testDir.getPath();
		}
		
		return null;
		
		
		
	}
	
	public static String getBasePath() {
		File testDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"");
		return testDir.getPath();
		
		
	}

	
	
	public static Shape getShape(int id) {
		return shapes[id];
	}

	public static Letter getLetter(int id) {
		return letters[id];
	}

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type, String s) {

		// Create a media file name
		File mediaFile;
		mediaFile = new File(getTestPath() + File.separator + s);

		return mediaFile;
	}
	
	public static File getOutputPicturesFile(int type, String s) {

		// Create a media file name
		File mediaFile;
		mediaFile = new File(getPicturesPath() + File.separator + s);

		return mediaFile;
	}

	public static String intToChar(int i) {
		char c = (char) (i + 65); // cast from int to char
		return String.valueOf(c);
	}

	public static void resetStage() {
		stage = 0;
	}

	public static float sqrdist(float x, float y, float xx, float yy) {
		return (float) (Math.pow(xx - x, 2) + Math.pow(yy - y, 2));
	}

	public static float dist(float x, float y, float xx, float yy) {
		return (float) Math.sqrt((Math.pow(xx - x, 2) + Math.pow(yy - y, 2)));
	}
	
	private static double[] lineIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		double[] ps = new double[2];
		double denom = ((x1-x2)*(y3-y4))-((y1-y2)*(x3-x4));
		double numx = ((x1*y2 - y1*x2)*(x3-x4)) - ((x1-x2)*(x3*y4-y3*x4));
		double numy = ((x1*y2 - y1*x2)*(y3-y4)) - ((y1-y2)*(x3*y4-y3*x4));
		Log.d("Rots", "Denom is "+denom);

		if(denom == 0) ps[0] = -1;
		else{
			ps[0] = numx / denom;
			ps[1] = numy / denom;
		}
		
		return ps;
	}
	

	public static double getRotation(float px, float py, float qx, float qy){
		double rots = 0f;
		
		float q1x, q1y, p1x, p1y;
		
		//associate x with last fingers
		float pdist = sqrdist(px, py, lastfinger1[0], lastfinger1[1]);
		float qdist = sqrdist(qx, qx, lastfinger1[0], lastfinger1[1]);
		
		if(qdist < pdist){
			q1x = lastfinger1[0];
			q1y = lastfinger1[1];
			p1x = lastfinger2[0];
			p1y = lastfinger2[1];
		}else{
			q1x = lastfinger2[0];
			q1y = lastfinger2[1];
			p1x = lastfinger1[0];
			p1y = lastfinger1[1];
		}
		
		lastfinger1[0] = px;
		lastfinger1[1] = py;
		lastfinger2[0] = qx;
		lastfinger2[1] = qy;
		
		
		if(q1x == -1) return 0.0;

		double[] isect = new double[2];
		isect = lineIntersection(px, py, qx, qy, p1x, p1y, q1x, q1y);
		Log.d("Rots", "Isect Pt is "+isect[0]+", "+isect[1]);


		
		
		if(isect[0] == -1) return 0; //these lines are parallel
		                            
//		double dot =  (qx-isect[0])*(q1x-isect[0]) + (qy-isect[1])*(q1y-isect[1]);
//		double magx = Math.sqrt(Math.pow((qx-isect[0]),2)+Math.pow((qy-isect[1]), 2));
//		double magy = Math.sqrt(Math.pow((q1x-isect[0]),2)+Math.pow((q1y-isect[1]), 2));
//		

//		if(magx*magy == 0 || (dot / (magx*magy)) > 1.0){
//			Log.d("Rots", "Returning Angle 0");
//			return 0;
//		}
		
		double signed_angle = Math.atan2(q1y-isect[1], q1x-isect[0]) - Math.atan2(qy-isect[1], qx-isect[0]);
		signed_angle *= -1;
		Log.d("Rots", "Signed Angle " +   Math.toDegrees(signed_angle));

		
		return Math.toDegrees(signed_angle);
	}
	
	
//	public static float getRotation(float x, float y, float x2, float y2) {
//
//		float[] finger1 = new float[2];
//		float[] finger2 = new float[2];
//
//		if (lastfinger1[0] == -1
//				|| sqrdist(x, y, lastfinger1[0], lastfinger1[1]) <= sqrdist(x,
//						y, lastfinger2[0], lastfinger2[1])) {
//			finger1[0] = x;
//			finger1[1] = y;
//			finger2[0] = x2;
//			finger2[1] = y2;
//		} else {
//			finger1[0] = x2;
//			finger1[1] = y2;
//			finger2[0] = x;
//			finger2[1] = y;
//		}
//
//		float dx = finger2[0] - finger1[0];
//		float dy = finger2[1] - finger2[0];
//
//		float angle = (float) Math.toDegrees(Math.atan(dy / dx));
//		Log.d("Matrix", "Returning Angle " + angle);
//
//		lastfinger1 = finger1;
//		lastfinger2 = finger2;
//
//		return angle;
//	}

	public static float getScale(float span) {
		float s = 0;
		if(lastSpan != -1){
			s = (float) (span- lastSpan);

			
		}
		lastSpan = span;

		return s;
	}

	public static float[] getCenter(float x, float y, float x2, float y2) {
		float[] center = new float[2];
		Rect r = new Rect((int) x, (int) y, (int) x2, (int) y2);
		center[0] = r.exactCenterX();
		center[1] = r.exactCenterY();

		return center;
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
        if (width > height) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        } else {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
    }
    return inSampleSize;
	}
	
	
	public static Bitmap decodeSampledBitmapFromResource(File f, int reqWidth, int reqHeight) {
			
//		reqWidth*= screen_density;
//		reqHeight*= screen_density;
		
		try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);
		
	        //Find the correct scale value. It should be the power of 2.
	        int scale=1;
	        while(o.outWidth/scale/2>=reqWidth && o.outHeight/scale/2>=reqHeight) scale*=2;
	
	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inScaled = false;
	       // o2.inDensity = (int)Globals.screen_density;
	        o2.inSampleSize=scale;
	        
	        
	       
	        Log.d("Density", "Screen"+Globals.screen_density);
	        Log.d("Density", "In Density"+o2.inDensity);
	        Log.d("Density", "Target Density"+o2.inTargetDensity);
			
	        
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;	  
		    
	}

	public static void changeDirectory(File dir) {
		mediaStorageDir = dir;
	}
	
	
	public static void writeToLog(Context context, String from, String to, double time){
//		String data_line = "";
//		
////		data_line = String.format("%d, %s, %s, %s, %f, %f, %f, %d, %b \n", line_num++, startDateTime, from, to, time, longitude, latitude, stage, 0);
//		data_line = String.format("%d, %s, %s, %s, %f, %f, %f, %d, %b \n", line_num++, startDateTime, from, to, time, 0f, 0f, stage, 0);
//		
//		if(getTestPath() != null){
//		File log = new File(getTestPath() + File.separator + "LOG.txt");
//		FileWriter fw;
//		try {
//			fw = new FileWriter(log, true);
//			if(save_string.length() != 0){
//				fw.write(save_string);
//				save_string = "";
//			}
//			fw.write(data_line);
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		}else{
//			save_string += data_line;
//		}

	
	}
	
	


}
