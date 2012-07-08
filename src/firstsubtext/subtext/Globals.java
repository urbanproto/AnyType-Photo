package firstsubtext.subtext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import data.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class Globals {
	static String timeStamp;
	static File mediaStorageDir;

	static Shape[] shapes;
	static Letter[] letters;
	static boolean[] existing_letters;
	static int stage;
	static float shapeStretch = 2.0f;
	static int letter_size = 600;
	static int grab_num = 0;
	static String base_dir_name;
	
	static BuildLettersTask build_thread = new BuildLettersTask();

	
	static boolean playback_mode = false;
	static int force_stage = 0;
	static int force_letter = 0;
	

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private static float[] lastfinger1 = new float[2];
	private static float[] lastfinger2 = new float[2];
	
	static LetterView saved_lv = null;
	

	public Globals(String time) {
		existing_letters = new boolean[26];
		for (int i = 0; i < existing_letters.length; i++) {
			existing_letters[i] = false;
		}

		timeStamp = time;
		mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"SI_" + timeStamp);
		
		base_dir_name = "SI_" + timeStamp;
		//base_dir_name = "testdir";
	

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
			}
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

		stage = 0;

	}
	
	public static boolean renameDirectory(String s){
		File file = new File(getTestPath());
		File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),s);
		boolean success = file.renameTo(file2);
		
		if(success) base_dir_name = s;
		
		return success;
	}
	
	public static boolean hasAnyVideos(){
		for(int i = 0; i < 5; i++){
			if(stageHasVideo(i)) return true;
		}
		return false;
	}

	public static boolean stageHasVideo(int stageid) {
		
		File vidFile = new File(getStageVideoPath(stageid));
		return vidFile.exists();

	}
	

	public static String getStageVideoPath(int stageid) {
		return (getTestPath() + File.separator + "VID_" + Integer.toString(stageid) + ".mp4");
	}

	
	public static boolean stageHasVideo() {
		
		File vidFile = new File(getStageVideoPath());
		return vidFile.exists();

	}
	
	public static String getStageVideoPath() {
		if(playback_mode) return (getTestPath() + File.separator + "VID_" + Integer.toString(force_stage) + ".mp4");
		else return (getTestPath() + File.separator + "VID_" + Integer.toString(stage) + ".mp4");
	}

	public static boolean buildLetters() {

		int w = letter_size; // this is based on 2 * the bounding box size
		int h = letter_size;
		int[] offset;
		Bitmap bmap;
		boolean make_letter;

		for (int i = 0; i < letters.length; i++) {

			make_letter = true;

			// make sure we have the shapes and are able to create this
			int[] shape_ids = letters[i].getShapeIds();
			for (int k = 0; k < shape_ids.length; k++) {
				if (shape_ids[k] >= stage) {
					make_letter = false;
					break;
				}
			}

			if (make_letter && !existing_letters[i]) {
				
				existing_letters[i] = true;
				
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
					bmap = Globals.decodeSampledBitmapFromResource(f,letter_size, letter_size);
				
					c.drawBitmap(bmap, new Matrix(), null);
					c.restore();

				}

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

				} catch (IllegalArgumentException e) {
					Log.d("Canvas", "Illegal Arg" + e.getMessage());
				}

			}
		}

		return true;
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
		File testDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				base_dir_name);
		return testDir.getPath();
		
		
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

	public static float getRotation(float x, float y, float x2, float y2) {

		float[] finger1 = new float[2];
		float[] finger2 = new float[2];

		if (lastfinger1[0] == -1
				|| sqrdist(x, y, lastfinger1[0], lastfinger1[1]) <= sqrdist(x,
						y, lastfinger2[0], lastfinger2[1])) {
			finger1[0] = x;
			finger1[1] = y;
			finger2[0] = x2;
			finger2[1] = y2;
		} else {
			finger1[0] = x2;
			finger1[1] = y2;
			finger2[0] = x;
			finger2[1] = y;
		}

		float dx = finger2[0] - finger1[0];
		float dy = finger2[1] - finger2[0];

		float angle = (float) Math.toDegrees(Math.atan(dy / dx));
		Log.d("Matrix", "Returning Angle " + angle);

		lastfinger1 = finger1;
		lastfinger2 = finger2;

		return angle;
	}

	public static float getScale(float x, float y, float x2, float y2) {

		// get the distance
		double hyp = 600 * Math.sqrt(2);
		double d = Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));

		float ratio = (float) ((float) d / hyp);

		if (ratio > 1.0)
			return 1.0f;
		if (ratio < 0.1)
			return 0.1f;
		return ratio;
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
		
		Bitmap b;

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    
	    try{

	    FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, options);
        fis.close();
        
	    }catch (IOException e){
	    	Log.d("BITMAP", e.getMessage());
	    }

	    // Calculate inSampleSize

	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    try{
	    FileInputStream fis  = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, options);
        fis.close();
        return b;
	    }catch (IOException e){
	    	Log.d("BITMAP", e.getMessage());
	    }
	    
	    return null;
	}

	public static void changeDirectory(File dir) {
		mediaStorageDir = dir;
	}

	


}
