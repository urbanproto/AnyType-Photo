package firstsubtext.subtext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import data.*;

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

public class Globals {
    static String timeStamp;
    static File mediaStorageDir;

    static Shape[] shapes;
    static Letter[] letters;
    static int stage;
    static float shapeStretch = 2.0f;
    
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

    public Globals(String time){
    	timeStamp = time;
    	mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "SI_"+timeStamp);
    	
    	if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
            }
        }

    	
    	//Load all of the shapes into an array for referencing into an array
    	shapes = new Shape[5];
    	for(int i = 0; i < 5; i++){
    		shapes[i] = new Shape(i);
    	}
    	
    	letters = new Letter[26];
    	for(int i = 0; i < 26; i++){
    		letters[i] = new Letter(i);
    	}
    	
    	stage = 0;
    	
    }
    
    public static boolean buildLetters(){

    		int w = 600; //this is based on 2 * the bounding box size
    		int h = 600;
    		int[] offset;
    		Bitmap bmap;

    		for(int i = 0; i < letters.length; i++){
    	
    		//get the pixels from the current screen
    		Bitmap  bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    		Canvas  c = new Canvas(bitmap);
    		
    		c.drawColor(Color.TRANSPARENT);
    		int[] x_points = letters[i].getXPoints();
    		int[] y_points = letters[i].getYPoints();
    		float[] rots = letters[i].getRotations();
    		int[] shape_ids = letters[i].getShapeIds();
    	
    		
    		for(int j = 0; j < x_points.length; j++){
        		
        		Matrix m = new Matrix();
        		
    			try{
    				//m.setScale(1/Globals.shapeStretch, 1/Globals.shapeStretch);  
    			}catch(Exception e){
    	    		Log.d("Canvas", "Matrix Execption");
    			}
    			
        		offset = shapes[shape_ids[j]].getOffset();
    			c.save();

    			c.translate(x_points[j]*Globals.shapeStretch, y_points[j]*Globals.shapeStretch);
    			c.rotate((int) Math.toDegrees(rots[j]));
    			c.translate(offset[0]*Globals.shapeStretch, offset[1]*Globals.shapeStretch);
    			
    			bmap = BitmapFactory.decodeFile(getTestPath() + File.separator +
    					"IMG_"+ Integer.toString(shape_ids[j]) + "_CROP.png");
    			c.drawBitmap(bmap, m, null);
    			c.restore();

    		}
    		
    		try{

    			Bitmap out = Bitmap.createBitmap(bitmap,0, 0, w, h, new Matrix(), false);
    			File pictureFile = Globals.getOutputMediaFile(MEDIA_TYPE_IMAGE, intToChar(i) + ".png");
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
					Log.d("Canvas", "Error accessing file: " + e.getMessage());
				}
				

				
    		}catch(IllegalArgumentException e){
    			Log.d("Canvas", "Illegal Arg"+e.getMessage());
    		}
    	

    		}

    	return true;
    }
    
    public static Shape getStageShape(){
    	return shapes[stage];
    }
    
    public static void nextStage(){
    	stage++;
    }
    
    public static String getTime(){
    	return timeStamp;
    }
    
    public static String getPath(){
    	return mediaStorageDir.getPath();
    }
    
    public static String getTestPath(){
    	File testDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "test");
    	return testDir.getPath();
    }
    
    public static Shape getShape(int id){
    	return shapes[id];
    }
    
    public static Letter getLetter(int id){
    	return letters[id];
    }
    
	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type, String s) {

		// Create a media file name
		File mediaFile;
		mediaFile = new File(getPath() + File.separator+ s);
		
		return mediaFile;
	}
	
	public static String intToChar(int i){
		char c = (char) (i+65); // cast from int to char
		return String.valueOf(c);    
	}
	
	public static void resetStage(){
		stage = 0;
	}
    
    
}
