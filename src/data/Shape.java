package data;

import java.io.*;
import java.util.*;

import firstsubtext.subtext.Globals;
import firstsubtext.subtext.R;
import android.app.Activity;
import android.graphics.*;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Shape extends Activity{

	private int  	id;
	private int[]   x_points;
	private int[]   y_points;
	private Globals g;
	private Bitmap  image;


	public Shape(int id, Globals g){
		this.id = id;
		this.g = g;
        Log.d("Shape", "Initialized "+id);
	}

	public void setPoints(int[] x, int[]y){
		x_points = x;
		y_points = y;
	}
	


	
	/**
	 * If this shape exists, load the edited shape
	 * @return whether or not the call was successful
	 */
	public boolean loadEditedShape(){
		return true;
	}


	/**
	 * Each time the user draws into the shape, use this function to update 
	 * the x and y coordinates
	 */
	public void updateShape(){

	}

	/**
	 * Return the image for this shape
	 */
	public Bitmap getImage(){
		return image;
	}

	/**
	 * This takes the captured image and converts it into the shape.  Must be called after capture image
	 * @return true if successful, false if not
	 */
	public boolean createShapeImage(){

		Paint 	p = new Paint();
		float[]   pts = getPoints();
		Bitmap 	base = Bitmap.createBitmap(1024, 800, Bitmap.Config.ARGB_8888);
		Canvas 	c = new Canvas(base);


		image = BitmapFactory.decodeFile(g.getPath() + File.separator +
				"IMG_"+ Integer.toString(id) + ".jpg");


		p.setARGB(255, 0, 0, 0);
		p.setStyle(Paint.Style.FILL);

		//draw this image to the bitmap with background white and foreground shape black
		c.drawARGB(255, 255, 255, 255);
		c.drawLines(pts, p);


		//go through and update the pixels 
		for(int i = 0; i < image.getWidth(); i++){
			for(int j = 0; j < image.getHeight(); j++){
				if(base.getPixel(i, j) == Color.WHITE) image.setPixel(i, j, Color.TRANSPARENT);	
			}
		}

		return true;
	}

	
	public float[] getPoints(){
		float[]   pts = new float[x_points.length + y_points.length];
		for(int i = 0; i < pts.length; i++){
			pts[i] = 0;
		}

		//convert x_points and y_points into a single array
		for(int i = 0; i < (x_points.length + y_points.length); i++){
			if(i % 2 == 0) pts[i] = x_points[i/2];
			else pts[i] = y_points[i/2];
		}
		
		System.out.println("Get Points Out: "+pts.length+" and  "+pts[0]);
		
		return pts;
	}
	
	public int getId(){
		return id;
	}

}
